# How to add NightBorne character animations

## Your GIF files → convert to PNG Sprite Sheets

### EASIEST: ezgif.com (no install needed)
1. Go to https://ezgif.com/gif-to-sprite
2. Upload NightBorne_idle.gif
3. Click "Convert to sprite sheet"
4. Download → rename to NightBorne_idle_sheet.png
5. Repeat for each GIF

### OR: ImageMagick command line
    magick NightBorne_idle.gif +append NightBorne_idle_sheet.png
    magick NightBorne_run.gif +append NightBorne_run_sheet.png
    magick NightBorne_attack.gif +append NightBorne_attack_sheet.png
    magick NightBorne_death.gif +append NightBorne_death_sheet.png
    magick NightBorne_hurt.gif +append NightBorne_hurt_sheet.png

## Put resulting files HERE (this folder):
    NightBorne_idle_sheet.png
    NightBorne_run_sheet.png
    NightBorne_attack_sheet.png
    NightBorne_death_sheet.png
    NightBorne_hurt_sheet.png

## Then update frame counts in:
    core/src/com/silksong/utils/AnimationManager.java
    Lines: FRAMES_IDLE, FRAMES_RUN, FRAMES_ATTACK, FRAMES_DEATH, FRAMES_HURT

Count frames = number of frames in original GIF

## Enemies: assets/textures/enemies/
    skeleton.png, bat.png

## Backgrounds: assets/textures/backgrounds/
    bg_gates.png (800x450)
    bg_great_hall.png (800x450)
    bg_throne_room.png (800x450)
