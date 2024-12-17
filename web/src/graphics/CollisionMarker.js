
export default class CollisionMarker {
  constructor(graphics) {
    this.graphics = graphics;
    this.markers = [];
  }

  addCollisionMarker(x, y, angle, time) {
    this.markers.push({ x, y, angle, time });
    this.graphics.fillStyle(0xff0000, 1);
    this.graphics.fillCircle(x, y, 5);
  }

  draw(currentTime) {
    this.graphics.clear();

    this.markers.forEach(marker => {
      this.graphics.fillStyle(0xff0000, 1);
      this.graphics.fillCircle(marker.x, marker.y, 5);
    });
  }
}