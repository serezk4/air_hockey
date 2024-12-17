import Puck from './entities/Puck.js';
import Paddle from './entities/Paddle.js';
import ScoreBoard from './ui/ScoreBoard.js';
import Controls from './ui/Controls.js';
import InfoPanel from './ui/InfoPanel.js';
import BotAI from './bot/BotAI.js';
import TrajectoryRenderer from './graphics/TrajectoryRenderer.js';
import CollisionMarker from './graphics/CollisionMarker.js';
import { GAME_WIDTH, GAME_HEIGHT, GAME_BOUNDARIES } from './config/constants.js';

export function create() {
  // **Светлый фон игрового поля**
  const fieldGraphics = this.add.graphics();
  fieldGraphics.fillStyle(0x010d21, 1); // Светло-серый фон
  fieldGraphics.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

  // **Центральная линия и круг (реалистичный стиль аэрохоккея)**
  fieldGraphics.lineStyle(6, 0xd0d0d0, 0.8);
  fieldGraphics.strokeCircle(GAME_WIDTH / 2, GAME_HEIGHT / 2, 80); // Центральный круг
  fieldGraphics.lineBetween(GAME_WIDTH / 2, 0, GAME_WIDTH / 2, GAME_HEIGHT); // Центральная линия

  // **Светящиеся границы**
  const borderGraphics = this.add.graphics();
  borderGraphics.lineStyle(10, 0x00bfa5, 1); // Светящиеся границы
  borderGraphics.strokeRect(
      GAME_BOUNDARIES.borderThickness / 2,
      GAME_BOUNDARIES.borderThickness / 2,
      GAME_WIDTH - GAME_BOUNDARIES.borderThickness,
      GAME_HEIGHT - GAME_BOUNDARIES.borderThickness
  );

  // **Реалистичные ворота**
  const goalHeight = 12;
  fieldGraphics.fillStyle(0xff5555, 0.8); // Красные ворота
  fieldGraphics.fillRect(
      GAME_WIDTH / 2 - GAME_BOUNDARIES.goalWidth / 2,
      GAME_BOUNDARIES.borderThickness / 2 - goalHeight / 2,
      GAME_BOUNDARIES.goalWidth,
      goalHeight
  );
  fieldGraphics.fillStyle(0x5555ff, 0.8); // Синие ворота
  fieldGraphics.fillRect(
      GAME_WIDTH / 2 - GAME_BOUNDARIES.goalWidth / 2,
      GAME_HEIGHT - GAME_BOUNDARIES.borderThickness / 2 - goalHeight / 2,
      GAME_BOUNDARIES.goalWidth,
      goalHeight
  );

  // **Создание стен для физики (невидимые)**
  const wallOptions = { isStatic: true, restitution: 1, friction: 0 };
  this.matter.add.rectangle(GAME_BOUNDARIES.borderThickness / 2, GAME_HEIGHT / 2, GAME_BOUNDARIES.borderThickness, GAME_HEIGHT, wallOptions);
  this.matter.add.rectangle(GAME_WIDTH - GAME_BOUNDARIES.borderThickness / 2, GAME_HEIGHT / 2, GAME_BOUNDARIES.borderThickness, GAME_HEIGHT, wallOptions);
  this.matter.add.rectangle(GAME_WIDTH / 2, GAME_BOUNDARIES.borderThickness / 2, GAME_WIDTH, GAME_BOUNDARIES.borderThickness, wallOptions);
  this.matter.add.rectangle(GAME_WIDTH / 2, GAME_HEIGHT - GAME_BOUNDARIES.borderThickness / 2, GAME_WIDTH, GAME_BOUNDARIES.borderThickness, wallOptions);

  // **Шайба и клюшки (реалистичный стиль)**
  this.puck = new Puck(this, GAME_WIDTH / 2, GAME_HEIGHT / 2, 'puck');
  this.playerPaddle = new Paddle(this, GAME_WIDTH / 2, GAME_HEIGHT - 100, 'paddle');
  this.botPaddle = new Paddle(this, GAME_WIDTH / 2, 50, 'paddle');

  this.puckGraphics = this.add.graphics();
  this.paddleGraphics = this.add.graphics();

  // **Таблица счёта**
  this.scoreBoard = new ScoreBoard(this, GAME_WIDTH - 150, GAME_HEIGHT - 40);

  // **Графика для траекторий и маркеров**
  this.trajectoryGraphics = this.add.graphics();
  this.trajectoryRenderer = new TrajectoryRenderer(
      this.trajectoryGraphics,
      this.puck,
      GAME_BOUNDARIES,
      this.botPaddle,
      { puckRestitution: 1 }
  );

  this.collisionMarker = new CollisionMarker(this.trajectoryGraphics);

  // **Информационная панель**
  this.infoPanel = new InfoPanel(document.getElementById('info'), {});

  this.botAI = new BotAI({
    difficulty: 3,
    botTrailLength: 100,
  }, this.botPaddle, this.puck, this);

  this.events.on('update', () => {
    this.puckGraphics.clear();
    this.puckGraphics.fillStyle(0x333333, 1);
    this.puckGraphics.fillCircle(this.puck.x, this.puck.y, 15);
    this.puckGraphics.fillStyle(0x000000, 0.2);
    this.puckGraphics.fillEllipse(this.puck.x, this.puck.y + 5, 20, 8);

    this.paddleGraphics.clear();
    this.paddleGraphics.fillStyle(0x4488ff, 1);
    this.paddleGraphics.fillCircle(this.playerPaddle.x, this.playerPaddle.y, 30);
    this.paddleGraphics.fillStyle(0x000000, 0.2);
    this.paddleGraphics.fillEllipse(this.playerPaddle.x, this.playerPaddle.y + 5, 35, 10);

    this.paddleGraphics.fillStyle(0xffaa00, 1);
    this.paddleGraphics.fillCircle(this.botPaddle.x, this.botPaddle.y, 30);
    this.paddleGraphics.fillStyle(0x000000, 0.2);
    this.paddleGraphics.fillEllipse(this.botPaddle.x, this.botPaddle.y + 5, 35, 10);
  });

  this.playerTarget = { x: this.playerPaddle.x, y: this.playerPaddle.y };
  this.input.on('pointermove', (pointer) => {
    const minY = GAME_HEIGHT / 2 + this.playerPaddle.body.circleRadius;
    const maxY = GAME_HEIGHT - this.playerPaddle.body.circleRadius;
    const minX = GAME_BOUNDARIES.borderThickness / 2 + this.playerPaddle.body.circleRadius;
    const maxX = GAME_WIDTH - GAME_BOUNDARIES.borderThickness / 2 - this.playerPaddle.body.circleRadius;

    this.playerTarget = {
      x: Phaser.Math.Clamp(pointer.x, minX, maxX),
      y: Phaser.Math.Clamp(pointer.y, minY, maxY),
    };
  });

  this.controls = new Controls(this, {
    onMassChange: (value) => this.puck.updateConfig({ mass: value }),
    onFrictionAirChange: (value) => this.puck.updateConfig({ frictionAir: value }),
    onRestitutionChange: (value) => this.puck.updateConfig({ restitution: value }),
    onVelocityChange: (value) => this.puck.updateConfig({ initialSpeed: value }),
    onResetPuck: () => this.puck.resetPosition(GAME_WIDTH / 2, GAME_HEIGHT / 2),
    onShowTrajectoryChange: (value) => this.showTrajectory = value,
    onShowBotThoughtsChange: (value) => this.showBotThoughts = value,
    onDifficultyChange: (value) => {
      this.botAI.config.difficulty = value;
      this.botAI.updateBotParameters();
    },
  });

  this.showTrajectory = true;
  this.showBotThoughts = false;
}
