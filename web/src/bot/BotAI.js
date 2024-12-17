import { BOT_CONFIG, GAME_WIDTH, GAME_HEIGHT, GAME_BOUNDARIES } from '../config/constants';

export default class BotAI {
  constructor(config, botPaddle, puck, scene) {
    this.config = { ...BOT_CONFIG, ...config };
    this.botPaddle = botPaddle;
    this.puck = puck;
    this.scene = scene;

    this.lastDecisionTime = 0;
    this.predictedX = null;
    this.predictedY = null;
    this.botTrail = [];

    this.updateBotParameters();
  }

  updateBotParameters() {
    this.botSpeed = this.config.baseSpeed * this.config.difficulty;
    this.botReactionTime = this.config.baseReaction / this.config.difficulty;
  }

  update(time) {
    if (time > this.lastDecisionTime + this.botReactionTime) {
      this.lastDecisionTime = time;
      this.makeDecision();
    }

    this.moveTowardsPrediction();
    this.recordTrail();
  }

  makeDecision() {
    const futureTime = this.botReactionTime / 1000;

    const puckX = this.puck.x;
    const puckY = this.puck.y;
    const puckVx = this.puck.body.velocity.x;
    const puckVy = this.puck.body.velocity.y;

    const predictedPuckX = puckX + puckVx * futureTime;
    const predictedPuckY = puckY + puckVy * futureTime;

    const puckInTopHalf = predictedPuckY < GAME_HEIGHT / 2;

    const closeToLeft = predictedPuckX < (GAME_BOUNDARIES.borderThickness / 2 + 50);
    const closeToRight = predictedPuckX > (GAME_WIDTH - GAME_BOUNDARIES.borderThickness / 2 - 50);

    let targetX, targetY;

    if (puckInTopHalf) {
      const playerGoalX = GAME_WIDTH / 2;
      const playerGoalY = GAME_HEIGHT - GAME_BOUNDARIES.borderThickness / 2;

      const dx = playerGoalX - predictedPuckX;
      const dy = playerGoalY - predictedPuckY;
      const dist = Math.sqrt(dx * dx + dy * dy);
      const nx = dx / dist;
      const ny = dy / dist;

      let attackDistance = 60;
      if (closeToLeft) {
        targetX = predictedPuckX - nx * attackDistance + 40;
      } else if (closeToRight) {
        targetX = predictedPuckX - nx * attackDistance - 40;
      } else {
        targetX = predictedPuckX - nx * attackDistance;
      }

      targetY = predictedPuckY - ny * attackDistance;
      if (targetY > GAME_HEIGHT / 2 - this.botPaddle.body.circleRadius) {
        targetY = GAME_HEIGHT / 2 - this.botPaddle.body.circleRadius;
      }

    } else {
      let offsetX = 0;
      if (closeToLeft) offsetX = 40;
      if (closeToRight) offsetX = -40;

      targetX = predictedPuckX + offsetX;
      targetY = predictedPuckY;
      if (targetY > GAME_HEIGHT / 2 - this.botPaddle.body.circleRadius) {
        targetY = GAME_HEIGHT / 2 - this.botPaddle.body.circleRadius;
      }
    }

    this.predictedX = targetX;
    this.predictedY = targetY;
  }

  moveTowardsPrediction() {
    if (this.predictedX != null && this.predictedY != null) {
      const dxBot = this.predictedX - this.botPaddle.x;
      const dyBot = this.predictedY - this.botPaddle.y;
      let botVx = 0;
      let botVy = 0;

      const moveSpeed = this.botSpeed * 1.5;
      if (Math.abs(dxBot) > 5) {
        botVx = (dxBot > 0) ? moveSpeed : -moveSpeed;
      }
      if (Math.abs(dyBot) > 5) {
        botVy = (dyBot > 0) ? moveSpeed : -moveSpeed;
      }

      this.botPaddle.setVelocity(botVx, botVy);
    }
  }

  recordTrail() {
    this.botTrail.push({ x: this.botPaddle.x, y: this.botPaddle.y });
    if (this.botTrail.length > this.config.botTrailLength) {
      this.botTrail.shift();
    }
  }

  getTrail() {
    return this.botTrail;
  }

  applyPushForce() {
    const distX = Math.abs(this.botPaddle.x - this.puck.x);
    const distY = Math.abs(this.botPaddle.y - this.puck.y);
    if (distX < 60 && distY < 30) {
      const playerGoalX = GAME_WIDTH / 2;
      const playerGoalY = GAME_HEIGHT - GAME_BOUNDARIES.borderThickness / 2;
      const dx = playerGoalX - this.puck.x;
      const dy = playerGoalY - this.puck.y;
      const length = Math.sqrt(dx * dx + dy * dy);
      const nx = dx / length;
      const ny = dy / length;

      const pushForce = 0.1 * this.config.difficulty;
      this.puck.setVelocity(
        this.puck.body.velocity.x + nx * pushForce,
        this.puck.body.velocity.y + ny * pushForce
      );
    }
  }
}

