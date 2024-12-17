export default class ScoreBoard {
  constructor(scene, x, y) {
    this.playerScore = 0;
    this.botScore = 0;

    this.playerText = scene.add.text(x, y, `Игрок: ${this.playerScore}`, { fontSize: '20px', fill: '#ffffff' });
    this.botText = scene.add.text(x, y - 30, `Бот: ${this.botScore}`, { fontSize: '20px', fill: '#ffffff' });
  }

  incrementPlayer() {
    this.playerScore++;
    this.updateText();
  }

  incrementBot() {
    this.botScore++;
    this.updateText();
  }

  updateText() {
    this.playerText.setText(`Игрок: ${this.playerScore}`);
    this.botText.setText(`Бот: ${this.botScore}`);
  }
}
