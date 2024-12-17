let frameCount = 0;
let lastTime = 0;

export function update(time, delta) {
    const fps = (1000 / delta).toFixed(1);
    frameCount++;

    this.scoreBoard.updateText();

    const dx = this.playerTarget.x - this.playerPaddle.x;
    const dy = this.playerTarget.y - this.playerPaddle.y;
    this.playerPaddle.setVelocity(dx * 0.3, dy * 0.3);

    this.botAI.update(time);
    this.botAI.applyPushForce();

    const botTrail = this.botAI.getTrail();

    this.trajectoryRenderer.draw(this.puck, this.botAI, this.showBotThoughts, this.showTrajectory, botTrail, time);

    this.infoPanel.updateInfo(
        this.puck,
        this.playerPaddle,
        this.botPaddle,
        this.collisionCount || 0,
        time,
        fps
    );

    lastTime = time;
}
