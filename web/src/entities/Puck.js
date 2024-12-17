import Phaser from 'phaser';
import { PUCK_CONFIG } from '../config/constants';

export default class Puck extends Phaser.Physics.Matter.Image {
  constructor(scene, x, y, texture) {
    super(scene.matter.world, x, y, texture, null, {
      restitution: PUCK_CONFIG.restitution,
      frictionAir: PUCK_CONFIG.frictionAir,
      friction: 0,
      frictionStatic: 0,
    });
    scene.add.existing(this);
    this.setCircle(PUCK_CONFIG.radius);
    this.setMass(PUCK_CONFIG.mass);
    this.initialSpeed = PUCK_CONFIG.initialSpeed;
  }

  resetPosition(x, y) {
    this.setPosition(x, y);
    this.setVelocity(0, 0);
    this.randomizeVelocity();
  }

  randomizeVelocity() {
    const angle = Phaser.Math.FloatBetween(0, Math.PI * 2);
    this.setVelocity(
      Math.cos(angle) * this.initialSpeed,
      Math.sin(angle) * this.initialSpeed
    );
  }

  updateConfig({ mass, frictionAir, restitution, initialSpeed }) {
    if (mass !== undefined) this.setMass(mass);
    if (frictionAir !== undefined) this.setFrictionAir(frictionAir);
    if (restitution !== undefined) this.setBounce(restitution);
    if (initialSpeed !== undefined) this.initialSpeed = initialSpeed;
  }
}

