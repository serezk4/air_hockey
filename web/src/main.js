import Phaser from 'phaser';
import { preload } from './preload.js';
import { create } from './create.js';
import { update } from './update.js';
import { GAME_WIDTH, GAME_HEIGHT } from './config/constants.js';

const config = {
  type: Phaser.AUTO,
  width: GAME_WIDTH,
  height: GAME_HEIGHT,
  backgroundColor: '#000000',
  physics: {
    default: 'matter',
    matter: {
      gravity: { y: 0 },
      debug: false,
    },
  },
  scene: {
    preload,
    create,
    update,
  },
};

const game = new Phaser.Game(config);

