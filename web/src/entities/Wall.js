import Phaser from 'phaser';

export default class Wall extends Phaser.Physics.Matter.Sprite {
  constructor(scene, x, y, width, height, options = {}) {
    super(scene.matter.world, x, y, null, null, {
      isStatic: true,
      restitution: 1,
      friction: 0,
      frictionStatic: 0,
      ...options,
    });
    scene.add.existing(this);
    this.setRectangle(width, height);
  }
}
