# 📁 SilkSong — Assets Guide

Поместите ваши файлы ресурсов в соответствующие папки.
Пути указаны в `AssetManagerSingleton.java`.

## 🖼️ Текстуры (PNG, рекомендуется 32-бит RGBA)

```
textures/
├── player/
│   ├── idle.png       — герой стоит (32×48 px)
│   ├── run.png        — герой бежит (32×48 px)
│   ├── jump.png       — герой в прыжке (32×48 px)
│   ├── attack.png     — атака мечом (48×48 px)
│   ├── dash.png       — рывок (32×48 px)
│   └── arrow.png      — стрела (24×6 px)
│
├── enemies/
│   ├── skeleton.png   — скелет (32×48 px)
│   └── bat.png        — летучая мышь (28×20 px)
│
├── boss/
│   ├── boss_phase1.png — синий Скелет-Король (80×120 px)
│   ├── boss_phase2.png — фиолетовый (80×120 px)
│   ├── boss_phase3.png — красный (80×120 px)
│   ├── ghost_sword.png — призрачный меч (32×12 px)
│   └── boss_shield.png — щит (40×40 px)
│
├── backgrounds/
│   ├── bg_gates.png        — улица перед замком (800×450 px)
│   ├── bg_great_hall.png   — зал замка (800×450 px)
│   └── bg_throne_room.png  — тронный зал (800×450 px)
│
├── misc/
│   ├── exp_orb.png    — сфера опыта (16×16 px) — зелёная
│   └── lever.png      — рычаг (24×40 px)
│
└── ui/
    ├── health_bar.png  — полоска HP (опционально)
    └── boss_health_bar.png
```

## 🎵 Музыка (MP3 или OGG, рекомендуется OGG для LibGDX)

```
audio/music/
├── menu_theme.mp3      — главное меню
├── gates_theme.mp3     — The Gates (мрачная, атмосферная)
├── great_hall_theme.mp3 — The Great Hall (напряжённая боевая)
└── boss_theme.mp3      — финальный бой (эпическая)
```

## 🔊 Звуковые эффекты (WAV или OGG, короткие)

```
audio/sfx/
├── sword_swing.wav     — свист меча
├── arrow_shoot.wav     — выстрел стрелы
├── player_hit.wav      — игрок получает удар
├── player_death.wav    — смерть игрока
├── enemy_hit.wav       — попадание по врагу
├── enemy_death.wav     — смерть врага
├── exp_collect.wav     — подбор сферы опыта
├── lever_pull.wav      — тяга рычага
├── boss_roar.wav       — рёв босса
├── boss_slam.wav       — удар циркулем
├── dash.wav            — рывок
├── jump.wav            — прыжок
└── level_up.wav        — повышение уровня
```

## 🔤 Шрифты

```
fonts/
└── dark_font.fnt       — BitmapFont в формате LibGDX AngelCode
                          (создаётся через Hiero tool из LibGDX)
```

## 💡 Советы

- Используйте **LibGDX Texture Packer** для упаковки спрайтов в атлас
- Все звуки OGG лучше MP3 по качеству/размеру в LibGDX  
- Бесплатные ресурсы: **itch.io**, **OpenGameArt.org**, **Kenney.nl**
- При отсутствии файла движок пишет WARN в лог, но не падает
