# 🗡️ SilkSong — Архитектурная документация

## Структура проекта

```
silksong/
├── core/src/com/silksong/
│   ├── SilkSongGame.java               — Главный класс (extends Game)
│   │
│   ├── screens/
│   │   ├── BaseGameScreen.java         — Базовый Screen (камера, batch, inputHandler)
│   │   ├── MainMenuScreen.java         — Главное меню
│   │   ├── GatesScreen.java            — Локация 1: The Gates (рычаг + ворота)
│   │   ├── GreatHallScreen.java        — Локация 2: The Great Hall (волны врагов)
│   │   ├── ThroneRoomScreen.java       — Локация 3: Throne Room (босс)
│   │   ├── GameOverScreen.java         — Смерть игрока
│   │   └── VictoryScreen.java          — Победа
│   │
│   ├── entities/
│   │   ├── player/
│   │   │   ├── Player.java             — Герой (Subject, Command target)
│   │   │   └── Arrow.java             — Стрела лука
│   │   ├── enemies/
│   │   │   ├── Enemy.java             — Базовый враг
│   │   │   ├── Skeleton.java          — Наземный враг
│   │   │   ├── Bat.java              — Летучая мышь
│   │   │   └── ExpOrb.java           — Сфера опыта
│   │   └── boss/
│   │       ├── Boss.java             — Скелет-Король (State machine)
│   │       └── GhostSword.java       — Призрачный меч (проджектайл фазы 2)
│   │
│   ├── patterns/
│   │   ├── command/
│   │   │   ├── Command.java          — Интерфейс команды
│   │   │   ├── PlayerCommands.java   — Все команды (Move, Jump, Dash, Attack...)
│   │   │   └── InputHandler.java     — Клавиши → Команды
│   │   ├── observer/
│   │   │   ├── ExpObserver.java      — Подписчик опыта
│   │   │   └── ExpSubject.java       — Издатель опыта
│   │   ├── factory/
│   │   │   └── EntityFactory.java    — Создание врагов и сфер
│   │   ├── state/
│   │   │   ├── PlayerStates.java     — Idle, Run, Jump, Attack, Dash
│   │   │   └── BossStates.java       — Defender, Shooter, Rage, Dead
│   │   └── strategy/
│   │       └── AttackStrategies.java — SwordStrategy, BowStrategy
│   │
│   ├── systems/
│   │   ├── assets/
│   │   │   └── AssetManagerSingleton.java  — [Singleton] Все ресурсы
│   │   ├── sound/
│   │   │   └── SoundManager.java           — [Singleton] Музыка + SFX + Fade
│   │   └── level/
│   │       └── LevelSystem.java            — [Observer] Уровень и характеристики
│   │
│   ├── ui/
│   │   └── UIHud.java               — [Observer] HP bar, EXP bar, Boss bar
│   ├── world/
│   │   └── Lever.java               — Рычаг с LeverListener
│   └── utils/
│       └── CameraShake.java         — Тряска камеры
│
└── desktop/src/com/silksong/desktop/
    └── DesktopLauncher.java         — main() для запуска
```

---

## 🧩 Паттерны проектирования

### 1. Singleton
```java
// AssetManagerSingleton — глобальный доступ к ресурсам
AssetManagerSingleton.getInstance().getTexture("textures/player/idle.png");

// SoundManager — глобальный звук с fade
SoundManager.getInstance().fadeToMusic(MUSIC_BOSS, 2f);
SoundManager.getInstance().playSwordSwing();
```

### 2. State Pattern (Player)
```
Player.currentState →  IdleState
                        RunState
                        JumpState
                        AttackState  ← активирует swordHitbox
                        DashState    ← непробиваемость + рывок
```
Переключение: `player.setState(new RunState())`

### 3. State Pattern (Boss — 3 фазы)
```
Boss.currentState → DefenderPhase  (100-70% HP) — синий,    ходит + рывок
                    ShooterPhase   (70-30% HP)  — фиолетовый, призрачные мечи
                    RagePhase      (<30% HP)    — красный,  серия + мыши + тряска
                    DeadState                  — смерть
```
Цвет свечения задаётся через `getGlowColor()` каждого состояния.

### 4. Command Pattern
```
InputHandler.handleInput()
    ├── A pressed    → MoveLeftCommand.execute()  → player.moveLeft()
    ├── Space just   → JumpCommand.execute()      → player.jump()
    ├── Shift just   → DashCommand.execute()      → player.dash()
    ├── J just       → SwordAttackCommand         → player.swordAttack()
    └── K just       → BowAttackCommand           → player.bowAttack()
```

### 5. Observer Pattern (Опыт)
```
Player (Subject)
  ├── addObserver(LevelSystem)   — повышает характеристики
  └── addObserver(UIHud)         — обновляет полосу опыта

При collectExp(amount):
  player.notifyObservers(amount)
    ├── levelSystem.onExpCollected(amount)  → levelUp() если достаточно
    └── hud.onExpCollected(amount)          → показывает "Level Up!"
```

### 6. Factory Method
```java
EntityFactory.createSkeleton(300f, 80f);
EntityFactory.createBat(400f, 200f);
EntityFactory.createOrbFromEnemy(deadEnemy);  // берёт expReward из врага
EntityFactory.createExpOrb(x, y, 200);        // для босса
```

### 7. Strategy Pattern (Атаки)
```java
// Меч — ближний бой
swordStrategy.performAttack(...)  → звук + активирует hitbox

// Лук — дальний бой  
bowStrategy.performAttack(...)    → создаёт Arrow в player.arrows
```

---

## 🎮 Поток игры

```
MainMenuScreen
    └── [ENTER] → создаём Player, LevelSystem, UIHud → GatesScreen
                                                          │
                    Находим Lever, тянем [E] → ворота открыты
                    player.x > 700 → GreatHallScreen
                                          │
                    3 волны врагов → все убиты
                    player.x > 750 → ThroneRoomScreen
                                          │
                    Бой с боссом (3 фазы)
                    boss.isDead()  → VictoryScreen
                    !player.isAlive() → GameOverScreen
                                          │
                    [ENTER] → MainMenuScreen
```

---

## 🔊 Добавление своих звуков

1. Поместите `.mp3` или `.wav` файл в `assets/audio/sfx/` или `assets/audio/music/`
2. Путь уже объявлен константой в `AssetManagerSingleton.java`
3. `loadAll()` автоматически его загрузит
4. Вызывайте через `SoundManager.getInstance().playXxx()`

Пример добавления нового звука:
```java
// 1. В AssetManagerSingleton:
public static final String SFX_NEW_ATTACK = "audio/sfx/new_attack.wav";

// 2. В loadAll():
loadSoundSafe(SFX_NEW_ATTACK);

// 3. В SoundManager:
public void playNewAttack() { playSound(AssetManagerSingleton.SFX_NEW_ATTACK); }

// 4. Вызов:
SoundManager.getInstance().playNewAttack();
```

---

## 📦 Сборка и запуск

```bash
# Собрать и запустить Desktop версию
./gradlew desktop:run

# Собрать JAR
./gradlew desktop:jar
# → desktop/build/libs/SilkSong-desktop-1.0.jar
```

**Требования:** Java 11+, LibGDX 1.12.1
