export default class Controls {
  constructor(scene, config) {
    this.scene = scene;
    this.config = config;
    this.setupControls();
  }

  setupControls() {
    const controlsDiv = document.getElementById('controls');

    const massLabel = document.createElement('label');
    massLabel.innerHTML = 'Масса шайбы: ';
    const massSlider = document.createElement('input');
    massSlider.id = 'massRange';
    massSlider.type = 'range';
    massSlider.min = '0.1';
    massSlider.max = '2';
    massSlider.step = '0.01';
    massSlider.value = '0.5';
    massLabel.appendChild(massSlider);
    controlsDiv.appendChild(massLabel);
    controlsDiv.appendChild(document.createElement('br'));

    const frictionAirLabel = document.createElement('label');
    frictionAirLabel.innerHTML = 'Трение воздуха: ';
    const frictionAirSlider = document.createElement('input');
    frictionAirSlider.id = 'frictionAirRange';
    frictionAirSlider.type = 'range';
    frictionAirSlider.min = '0';
    frictionAirSlider.max = '0.01';
    frictionAirSlider.step = '0.001';
    frictionAirSlider.value = '0';
    frictionAirLabel.appendChild(frictionAirSlider);
    controlsDiv.appendChild(frictionAirLabel);
    controlsDiv.appendChild(document.createElement('br'));

    const restitutionLabel = document.createElement('label');
    restitutionLabel.innerHTML = 'Упругость: ';
    const restitutionSlider = document.createElement('input');
    restitutionSlider.id = 'restitutionRange';
    restitutionSlider.type = 'range';
    restitutionSlider.min = '0';
    restitutionSlider.max = '1';
    restitutionSlider.step = '0.01';
    restitutionSlider.value = '1';
    restitutionLabel.appendChild(restitutionSlider);
    controlsDiv.appendChild(restitutionLabel);
    controlsDiv.appendChild(document.createElement('br'));

    const velocityLabel = document.createElement('label');
    velocityLabel.innerHTML = 'Нач. скорость шайбы: ';
    const velocitySlider = document.createElement('input');
    velocitySlider.id = 'velocityRange';
    velocitySlider.type = 'range';
    velocitySlider.min = '0.5';
    velocitySlider.max = '5';
    velocitySlider.step = '0.1';
    velocitySlider.value = '2';
    velocityLabel.appendChild(velocitySlider);
    controlsDiv.appendChild(velocityLabel);
    controlsDiv.appendChild(document.createElement('br'));

    const resetPuckBtn = document.createElement('button');
    resetPuckBtn.id = 'resetPuckBtn';
    resetPuckBtn.innerText = 'Сбросить шайбу';
    resetPuckBtn.style = 'margin-top:0px;margin-bottom:15px;background:#4caf50;border:none;padding:5px 10px;border-radius:5px;color:#fff;cursor:pointer;';
    controlsDiv.appendChild(resetPuckBtn);
    controlsDiv.appendChild(document.createElement('br'));
    
    const showTrajectoryLabel = document.createElement('label');
    showTrajectoryLabel.innerHTML = 'Показывать траекторию: ';
    const showTrajectoryCheck = document.createElement('input');
    showTrajectoryCheck.id = 'showTrajectoryCheck';
    showTrajectoryCheck.type = 'checkbox';
    showTrajectoryCheck.checked = true;
    showTrajectoryLabel.appendChild(showTrajectoryCheck);
    controlsDiv.appendChild(showTrajectoryLabel);
    controlsDiv.appendChild(document.createElement('br'));

    const showBotThoughtsLabel = document.createElement('label');
    showBotThoughtsLabel.innerHTML = 'Показывать мысли бота: ';
    const showBotThoughtsCheck = document.createElement('input');
    showBotThoughtsCheck.id = 'showBotThoughtsCheck';
    showBotThoughtsCheck.type = 'checkbox';
    showBotThoughtsCheck.checked = false;
    showBotThoughtsLabel.appendChild(showBotThoughtsCheck);
    controlsDiv.appendChild(showBotThoughtsLabel);
    controlsDiv.appendChild(document.createElement('br'));

    const difficultyLabel = document.createElement('label');
    difficultyLabel.innerHTML = 'Сложность бота: ';
    const difficultySlider = document.createElement('input');
    difficultySlider.id = 'difficultyRange';
    difficultySlider.type = 'range';
    difficultySlider.min = '1';
    difficultySlider.max = '5';
    difficultySlider.step = '1';
    difficultySlider.value = '3';
    difficultyLabel.appendChild(difficultySlider);
    controlsDiv.appendChild(difficultyLabel);
    controlsDiv.appendChild(document.createElement('br'));

    showBotThoughtsCheck.addEventListener('change', () => {
      this.config.onShowBotThoughtsChange(showBotThoughtsCheck.checked);
    });

    massSlider.addEventListener('input', () => {
      const value = parseFloat(massSlider.value);
      this.config.onMassChange(value);
    });

    frictionAirSlider.addEventListener('input', () => {
      const value = parseFloat(frictionAirSlider.value);
      this.config.onFrictionAirChange(value);
    });

    restitutionSlider.addEventListener('input', () => {
      const value = parseFloat(restitutionSlider.value);
      this.config.onRestitutionChange(value);
    });

    velocitySlider.addEventListener('input', () => {
      const value = parseFloat(velocitySlider.value);
      this.config.onVelocityChange(value);
    });

    resetPuckBtn.addEventListener('click', () => {
      this.config.onResetPuck();
    });

    showTrajectoryCheck.addEventListener('change', () => {
      this.config.onShowTrajectoryChange(showTrajectoryCheck.checked);
    });

    difficultySlider.addEventListener('input', () => {
      const value = parseInt(difficultySlider.value);
      this.config.onDifficultyChange(value);
    });
  }
}

