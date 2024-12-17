export const GAME_WIDTH = 800;
export const GAME_HEIGHT = 600;

export const PUCK_CONFIG = {
  mass: 0.5,
  frictionAir: 0,
  restitution: 1,
  initialSpeed: 2,
  radius: 15,
};

export const PADDLE_CONFIG = {
  radius: 25,
  frictionAir: 0.005,
  restitution: 1,
  mass: 1,
};

export const BOT_CONFIG = {
  baseSpeed: 2,
  baseReaction: 600,
  difficulty: 3,
};

export const GAME_BOUNDARIES = {
  borderThickness: 30,
  goalWidth: 200,
  goalHeight: 10,
};

export const COLLISION_DISPLAY_TIME = 2000; // ms
export const BOT_TRAIL_LENGTH = 100; // frames

