import Phaser from 'phaser';
import { GAME_WIDTH, GAME_HEIGHT, GAME_BOUNDARIES, COLLISION_DISPLAY_TIME } from '../config/constants.js';

export default class TrajectoryRenderer {
  constructor(graphics, puck, gameBounds, botPaddle, config) {
    this.graphics = graphics;
    this.puck = puck;
    this.botPaddle = botPaddle; // Добавлено
    this.gameBounds = gameBounds;
    this.config = config;

    this.collisionMarkers = [];
  }

  addCollisionMarker(x, y, angle, timestamp) {
    this.collisionMarkers.push({ x, y, angle, t: timestamp });
  }

  drawCollisions(currentTime) {
    this.collisionMarkers = this.collisionMarkers.filter(m => (currentTime - m.t) < COLLISION_DISPLAY_TIME);

    for (let marker of this.collisionMarkers) {
      const age = currentTime - marker.t;
      const alpha = Phaser.Math.Clamp(1 - age / COLLISION_DISPLAY_TIME, 0, 1);
      this.graphics.fillStyle(0x00ff00, alpha);
      this.graphics.fillCircle(marker.x, marker.y, 5);

      const lineLen = 20;
      const endX = marker.x + Math.cos(marker.angle) * lineLen;
      const endY = marker.y + Math.sin(marker.angle) * lineLen;

      this.graphics.lineStyle(2, 0x00ff00, alpha);
      this.graphics.beginPath();
      this.graphics.moveTo(marker.x, marker.y);
      this.graphics.lineTo(endX, endY);
      this.graphics.strokePath();
    }
  }

  drawPuckTrajectory() {
    const maxCollisions = 3;
    let collisionCount = 0;

    const steps = 1000;
    const timeStep = 0.01;

    let simX = this.puck.x;
    let simY = this.puck.y;
    let simVx = this.puck.body.velocity.x;
    let simVy = this.puck.body.velocity.y;

    const leftBound = this.gameBounds.borderThickness / 2 + this.puck.body.circleRadius;
    const rightBound = GAME_WIDTH - this.gameBounds.borderThickness / 2 - this.puck.body.circleRadius;
    const topBound = this.gameBounds.borderThickness / 2 + this.puck.body.circleRadius;
    const bottomBound = GAME_HEIGHT - this.gameBounds.borderThickness / 2 - this.puck.body.circleRadius;

    this.graphics.lineStyle(2, 0xffff00, 0.7);
    this.graphics.beginPath();
    this.graphics.moveTo(simX, simY);

    const arrowInterval = 50;
    let stepsSinceArrow = 0;

    for (let i = 0; i < steps; i++) {
      simX += simVx * timeStep;
      simY += simVy * timeStep;

      let collided = false;
      if (simX < leftBound) {
        simX = leftBound;
        simVx = -simVx * this.puck.restitution;
        collided = true;
      }
      if (simX > rightBound) {
        simX = rightBound;
        simVx = -simVx * this.puck.restitution;
        collided = true;
      }
      if (simY < topBound) {
        simY = topBound;
        simVy = -simVy * this.puck.restitution;
        collided = true;
      }
      if (simY > bottomBound) {
        simY = bottomBound;
        simVy = -simVy * this.puck.restitution;
        collided = true;
      }

      if (collided) {
        collisionCount++;
        if (collisionCount > maxCollisions) {
          break;
        }
      }

      this.graphics.lineTo(simX, simY);

      stepsSinceArrow++;
      if (stepsSinceArrow === arrowInterval) {
        stepsSinceArrow = 0;
        const angle = Math.atan2(simVy, simVx);
        const arrowLen = 10;
        const arrowX = simX;
        const arrowY = simY;
        const endX = arrowX + Math.cos(angle) * arrowLen;
        const endY = arrowY + Math.sin(angle) * arrowLen;

        this.graphics.strokePath();
        this.graphics.beginPath();
        this.graphics.lineStyle(2, 0xff0000, 1);
        this.graphics.moveTo(arrowX, arrowY);
        this.graphics.lineTo(endX, endY);
        this.graphics.strokePath();

        const headLen = 5;
        const leftAngle = angle + Math.PI * 2 / 3;
        const rightAngle = angle - Math.PI * 2 / 3;

        this.graphics.beginPath();
        this.graphics.moveTo(endX, endY);
        this.graphics.lineTo(endX - Math.cos(leftAngle) * headLen, endY - Math.sin(leftAngle) * headLen);
        this.graphics.lineTo(endX - Math.cos(rightAngle) * headLen, endY - Math.sin(rightAngle) * headLen);
        this.graphics.closePath();
        this.graphics.fillStyle(0xff0000, 1);
        this.graphics.fillPath();

        this.graphics.beginPath();
        this.graphics.lineStyle(2, 0xffff00, 0.7);
        this.graphics.moveTo(simX, simY);
      }
    }

    this.graphics.strokePath();

    // Стрелка текущей скорости шайбы
    const angle = Math.atan2(this.puck.body.velocity.y, this.puck.body.velocity.x);
    const arrowLength = 50;
    const arrowX = this.puck.x;
    const arrowY = this.puck.y;
    const endX = arrowX + Math.cos(angle) * arrowLength;
    const endY = arrowY + Math.sin(angle) * arrowLength;

    this.graphics.lineStyle(3, 0xff0000, 1);
    this.graphics.beginPath();
    this.graphics.moveTo(arrowX, arrowY);
    this.graphics.lineTo(endX, endY);
    this.graphics.strokePath();

    const headLength = 10;
    const leftAngleHead = angle + Math.PI * 2 / 3;
    const rightAngleHead = angle - Math.PI * 2 / 3;
    this.graphics.beginPath();
    this.graphics.moveTo(endX, endY);
    this.graphics.lineTo(endX - Math.cos(leftAngleHead) * headLength, endY - Math.sin(leftAngleHead) * headLength);
    this.graphics.lineTo(endX - Math.cos(rightAngleHead) * headLength, endY - Math.sin(rightAngleHead) * headLength);
    this.graphics.closePath();
    this.graphics.fillStyle(0xff0000, 1);
    this.graphics.fillPath();
  }

  drawBotThoughts(botAI) {
    if (this.botPaddle && botAI.predictedX != null && botAI.predictedY != null) {
      this.graphics.lineStyle(2, 0xff00ff, 0.8);
      this.graphics.beginPath();
      this.graphics.moveTo(this.botPaddle.x, this.botPaddle.y);
      this.graphics.lineTo(botAI.predictedX, botAI.predictedY);
      this.graphics.strokePath();
    }

    const playerGoalX = GAME_WIDTH / 2;
    const playerGoalY = GAME_HEIGHT - GAME_BOUNDARIES.borderThickness / 2;
    this.graphics.lineStyle(2, 0xff00ff, 0.8);
    this.graphics.beginPath();
    this.graphics.moveTo(this.puck.x, this.puck.y);
    this.graphics.lineTo(playerGoalX, playerGoalY);
    this.graphics.strokePath();
  }

  drawBotTrail(botTrail) {
    this.graphics.lineStyle(2, 0x00ffff, 0.8);
    this.graphics.beginPath();
    if (botTrail.length > 0) {
      this.graphics.moveTo(botTrail[0].x, botTrail[0].y);
      for (let i = 1; i < botTrail.length; i++) {
        this.graphics.lineTo(botTrail[i].x, botTrail[i].y);
      }
    }
    this.graphics.strokePath();
  }

  drawFrictionForce() {
    const frictionForce = this.puck.body.frictionAir * Math.sqrt(this.puck.body.velocity.x ** 2 + this.puck.body.velocity.y ** 2);
    const angle = Math.atan2(this.puck.body.velocity.y, this.puck.body.velocity.x) + Math.PI; // Направление противоположно скорости
    const arrowLength = 50; // Длина стрелки

    const arrowX = this.puck.x;
    const arrowY = this.puck.y;
    const endX = arrowX + Math.cos(angle) * arrowLength;
    const endY = arrowY + Math.sin(angle) * arrowLength;

    this.graphics.lineStyle(3, 0x0000ff, 1); // Синий цвет для силы трения
    this.graphics.beginPath();
    this.graphics.moveTo(arrowX, arrowY);
    this.graphics.lineTo(endX, endY);
    this.graphics.strokePath();

    // Рисование наконечника стрелки
    const headLength = 10;
    const leftAngle = angle + Math.PI * 2 / 3;
    const rightAngle = angle - Math.PI * 2 / 3;

    this.graphics.beginPath();
    this.graphics.moveTo(endX, endY);
    this.graphics.lineTo(endX - Math.cos(leftAngle) * headLength, endY - Math.sin(leftAngle) * headLength);
    this.graphics.lineTo(endX - Math.cos(rightAngle) * headLength, endY - Math.sin(rightAngle) * headLength);
    this.graphics.closePath();
    this.graphics.fillStyle(0x0000ff, 1);
    this.graphics.fillPath();
  }


  draw(puck, botAI, showBotThoughts, showTrajectory, botTrail, currentTime) {
    this.graphics.clear();
    if (showTrajectory) {
      this.drawPuckTrajectory();
      this.drawFrictionForce();
    }
    this.drawCollisions(currentTime);
    if (showBotThoughts) {
      this.drawBotThoughts(botAI);
      this.drawBotTrail(botTrail);
    }
  }
}

