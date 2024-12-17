export default class InfoPanel {
    constructor(infoDiv, config) {
        this.infoDiv = infoDiv;
        this.config = config;
        this.formulas = this.getFormulas();
        this.showFormulas = false;
        this.setupPanel();
        this.setupToggle();
    }

    setupToggle() {
        const toggleFormulas = document.getElementById('toggleFormulas');
        if (!toggleFormulas) return;

        toggleFormulas.addEventListener('change', (event) => {
            this.showFormulas = event.target.checked;
            this.updateFormulasVisibility();
        });
    }

    updateFormulasVisibility() {
        this.formulas.forEach(formula => {
            const formulaContainer = document.getElementById(`formula-container-${formula.id}`);
            if (formulaContainer) {
                formulaContainer.style.display = this.showFormulas ? 'block' : 'none';
            }
        });

        if (window.MathJax) {
            if (this.showFormulas) {
                window.MathJax.typesetPromise();
            }
        }
    }

    getFormulas() {
        return [
            {
                id: 'second-law',
                name: 'Второй закон Ньютона',
                textFormula: 'F = m × a',
                formula: 'F = m \\times a',
                description: 'Определяет силу, действующую на объект, исходя из его массы и ускорения.',
                calculate: ({mass, acceleration}) => {
                    if (mass === undefined || acceleration === undefined) return 'N/A';
                    return `${mass.toFixed(2)} кг × ${acceleration.toFixed(2)} м/с² = ${(mass * acceleration).toFixed(2)} Н`;
                },
            },
            {
                id: 'momentum',
                name: 'Сохранение импульса',
                textFormula: 'p = m × v',
                formula: 'p = m \\times v',
                description: 'Импульс объекта равен произведению его массы на скорость.',
                calculate: ({mass, speed}) => {
                    if (mass === undefined || speed === undefined) return 'N/A';
                    return `${mass.toFixed(2)} кг × ${speed.toFixed(2)} м/с = ${(mass * speed).toFixed(2)} кг·м/с`;
                },
            },
            {
                id: 'kinetic-energy',
                name: 'Кинетическая энергия',
                textFormula: 'KE = 0.5 × m × v²',
                formula: 'KE = 0.5 \\times m \\times v^2',
                description: 'Определяет кинетическую энергию объекта.',
                calculate: ({mass, speed}) => {
                    if (mass === undefined || speed === undefined) return 'N/A';
                    return `0.5 × ${mass.toFixed(2)} кг × (${speed.toFixed(2)} м/с)² = ${(0.5 * mass * speed * speed).toFixed(2)} Дж`;
                },
            },
            {
                id: 'angle-of-motion',
                textFormula: 'θ = arctan(Vy / Vx)',
                name: 'Угол движения',
                formula: '\\theta = \\arctan\\left(\\frac{V_y}{V_x}\\right)',
                description: 'Определяет направление движения объекта относительно горизонтали.',
                calculate: ({vy, vx}) => {
                    if (vy === undefined || vx === undefined || vx === 0) return 'N/A';
                    const angle = Math.atan2(vy, vx) * (180 / Math.PI); // Угол в градусах
                    return `arctan(${vy.toFixed(2)} / ${vx.toFixed(2)}) = ${angle.toFixed(1)}°`;
                },
            },
            {
                id: 'air-friction',
                name: 'Сила воздушного трения',
                textFormula: 'F_{air} = k × v',
                formula: 'F_{air} = k \\times v',
                description: 'Определяет силу, действующую на объект из-за воздушного трения.',
                calculate: ({frictionAir, speed}) => {
                    if (frictionAir === undefined || speed === undefined) return 'N/A';
                    return `${frictionAir.toFixed(3)} кг/с × ${speed.toFixed(2)} м/с = ${(frictionAir * speed).toFixed(2)} Н`;
                },
            },
            {
                id: 'acceleration',
                name: 'Ускорение от силы',
                textFormula: 'a = F / m',
                formula: 'a = F / m',
                description: 'Определяет ускорение объекта при действии силы.',
                calculate: ({force, mass}) => {
                    if (force === undefined || mass === undefined || mass <= 0) return 'N/A';
                    return `${force.toFixed(2)} Н / ${mass.toFixed(2)} кг = ${(force / mass).toFixed(2)} м/с²`;
                },
            },
            {
                id: 'impulse',
                name: 'Импульс',
                textFormula: 'J = F × Δt = Δp',
                formula: 'J = F \\times \\Delta t = \\Delta p',
                description: 'Импульс равен произведению силы на время её действия.',
                calculate: ({force, deltaTime}) => {
                    if (force === undefined || deltaTime === undefined) return 'N/A';
                    const deltaP = force * deltaTime;
                    return `${force.toFixed(2)} Н × ${deltaTime.toFixed(2)} с = ${deltaP.toFixed(2)} кг·м/с`;
                },
            },
        ];
    }

    setupPanel() {
        this.parametersDiv = document.createElement('div');
        this.parametersDiv.id = 'parameters';
        this.parametersDiv.innerHTML = `<div><strong>Текущие параметры:</strong></div>`;
        this.infoDiv.appendChild(this.parametersDiv);

        this.formulasDiv = document.createElement('div');
        this.formulasDiv.id = 'formulas';
        this.formulasDiv.innerHTML = `<div><strong>Физические Формулы и Вычисления:</strong></div>`;
        this.infoDiv.appendChild(this.formulasDiv);

        this.formulas.forEach(formula => {
            const formulaContainer = document.createElement('div');
            formulaContainer.classList.add('formula-container');
            formulaContainer.id = `formula-container-${formula.id}`;

            const header = document.createElement('div');
            header.classList.add('formula-header');
            header.innerHTML = `<strong>${formula.textFormula}</strong> = <span id="${formula.id}-value">0</span>`;

            const content = document.createElement('div');
            content.classList.add('formula-content');
            content.innerHTML = `
      <p><strong>${formula.name}:</strong> ${formula.description}</p>
      <p><strong>Вычисление:</strong> ${formula.calculate({})}</p>
      <p><strong>Формула:</strong> $$${formula.formula}$$</p>
    `;

            header.addEventListener('click', () => {
                content.classList.toggle('collapsed');
                header.classList.toggle('active');
            });

            formulaContainer.appendChild(header);
            formulaContainer.appendChild(content);
            this.formulasDiv.appendChild(formulaContainer);
        });
    }

    updateInfo(puck, playerPaddle, botPaddle, collisionCount, time, fps) {
        if (!puck || !puck.body) {
            console.error('Puck, physics world, or puck.body is undefined');
            return;
        }

        const vx = puck.body.velocity.x;
        const vy = puck.body.velocity.y;
        const speed = Math.sqrt(vx * vx + vy * vy);
        const speedAngle = (Math.atan2(vy, vx) * 180) / Math.PI;

        const puckPosition = {x: puck.x.toFixed(2), y: puck.y.toFixed(2)};
        const playerPosition = {x: playerPaddle.x.toFixed(2), y: playerPaddle.y.toFixed(2)};
        const botPosition = {x: botPaddle.x.toFixed(2), y: botPaddle.y.toFixed(2)};

        const botImpulse = this.calculateBotImpulse(botPaddle, puck);
        this.parametersDiv.innerHTML = `
    <div><strong>Состояние симуляции:</strong></div>
    <div>FPS: ${fps}</div>
    <div>Время симуляции: ${(time / 1000).toFixed(2)} сек</div>
    <div>Скорость шайбы: ${speed.toFixed(2)} м/с</div>
    <div>Угол движения шайбы: ${speedAngle.toFixed(1)}°</div>
    <div>Положение шайбы: X=${puckPosition.x}, Y=${puckPosition.y}</div>
    <div>Положение игрока: X=${playerPosition.x}, Y=${playerPosition.y}</div>
    <div>Положение бота: X=${botPosition.x}, Y=${botPosition.y}</div>
    <div>Количество столкновений: ${collisionCount}</div>
    <div>Импульс удара бота: ${botImpulse.toFixed(2)} кг·м/с</div>
  `;

        if (!this.showFormulas) return;

        const mass = puck.body.mass;
        const frictionAir = puck.body.frictionAir;
        const restitution = puck.body.restitution;
        const initialSpeed = puck.initialSpeed;

        if (mass === undefined || frictionAir === undefined || restitution === undefined || initialSpeed === undefined) {
            console.error('One or more puck properties are undefined');
            return;
        }

        const forceAir = frictionAir * speed; // F_air = k × v
        const momentum = mass * speed; // p = m × v
        const kineticEnergy = 0.5 * mass * speed * speed; // KE = 0.5 × m × v²
        const acceleration = mass > 0 ? forceAir / mass : 0; // a = F / m
        const deltaTime = frictionAir > 0 ? 1 / frictionAir : 0;
        const impulse = frictionAir > 0 ? forceAir * deltaTime : 0; // J = F × Δt

        this.updateFormulas({
            mass,
            frictionAir,
            restitution,
            initialSpeed,
            vx,
            vy,
            speed,
            speedAngle,
            force: forceAir,
            momentum,
            kineticEnergy,
            acceleration,
            deltaTime,
            impulse,
        });
    }

    calculateBotImpulse(botPaddle, puck) {
        if (!botPaddle || !puck || !puck.body || !botPaddle.body) return 0;

        const relativeVelocityX = botPaddle.body.velocity.x - puck.body.velocity.x;
        const relativeVelocityY = botPaddle.body.velocity.y - puck.body.velocity.y;

        const relativeSpeed = Math.sqrt(relativeVelocityX ** 2 + relativeVelocityY ** 2);
        const puckMass = puck.body.mass;

        return puckMass * relativeSpeed; // Импульс = масса * скорость
    }

    updateFormulas(params) {
        this.formulas.forEach(formula => {
            const formulaContainer = document.getElementById(`formula-container-${formula.id}`);
            if (!formulaContainer) {
                console.error(`Formula container with id formula-container-${formula.id} not found`);
                return;
            }

            const valueElement = document.getElementById(`${formula.id}-value`);
            if (!valueElement) {
                console.error(`Value element with id ${formula.id}-value not found`);
                return;
            }

            let calculatedValue = '';
            calculatedValue = formula.calculate(params);
            if (calculatedValue !== 'N/A') {
                const splitValue = calculatedValue.split(' = ');
                if (splitValue.length === 2) {
                    valueElement.textContent = splitValue[1];
                } else {
                    valueElement.textContent = calculatedValue;
                }
            } else {
                valueElement.textContent = 'N/A';
            }

            const content = formulaContainer.querySelector('.formula-content');
            if (content) {
                content.innerHTML = `
          <p><strong>${formula.name}:</strong> ${formula.description}</p>
          <p><strong>Вычисление:</strong> ${calculatedValue !== 'N/A' ? calculatedValue : 'N/A'}</p>
          <p><strong>Формула:</strong> $$${formula.formula}$$</p>
        `;
            }
        });

        if (window.MathJax) {
            window.MathJax.typesetPromise();
        }
    }
}
