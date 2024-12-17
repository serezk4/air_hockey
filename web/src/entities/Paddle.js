import Phaser from 'phaser';
import { PADDLE_CONFIG } from '../config/constants';

export default class Paddle extends Phaser.Physics.Matter.Image {
  constructor(scene, x, y, texture) {
    super(scene.matter.world, x, y, texture, null, {
      isStatic: false,
      restitution: PADDLE_CONFIG.restitution,
      frictionAir: PADDLE_CONFIG.frictionAir,
      friction: 0,
      frictionStatic: 0,
    });
    scene.add.existing(this);
    this.setCircle(PADDLE_CONFIG.radius);
    this.setFixedRotation();
    this.setMass(PADDLE_CONFIG.mass);
  }
}
