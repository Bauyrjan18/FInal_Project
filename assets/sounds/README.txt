══════════════════════════════════════════════════════════════
  SOUND EFFECTS — Place your .wav or .mp3 files here
══════════════════════════════════════════════════════════════

File name              | When it plays
-----------------------|----------------------------------------
sword_swing.wav        | Player attacks with sword (J key)
arrow_shoot.wav        | Player shoots arrow (K key)
player_hurt.wav        | Player takes damage from enemy
player_death.wav       | Player dies (HP = 0)
player_jump.wav        | Player jumps (W / Space)
player_dash.wav        | Player dashes (Shift)
enemy_hit.wav          | Any enemy takes damage
enemy_death.wav        | Enemy is killed
exp_collect.wav        | Player collects GREEN XP orb ← YOU ASKED THIS
level_up.wav           | Player levels up
boss_roar.wav          | Boss appears (Throne Room entry)
boss_phase.wav         | Boss changes phase (purple / red glow)
soul_wave.wav          | Super attack fired (J when bar full)
menu_open.wav          | Pause menu opens (ESC key) ← YOU ASKED THIS
victory.wav            | Victory screen appears (boss killed) ← YOU ASKED THIS
game_over.wav          | Game Over screen appears (player dies) ← YOU ASKED THIS

══════════════════════════════════════════════════════════════
  HOW TO ADD FOOTSTEP SOUND
══════════════════════════════════════════════════════════════
  1. Place file: assets/sounds/footstep.wav
  2. Open: core/src/com/silksong/systems/sound/SoundManager.java
  3. Find "private Sound sfxMenuOpen;" and add below it:
       private Sound sfxFootstep;
  4. Find "sfxMenuOpen = tryLoadSound(F_MENU_OPEN);" and add:
       sfxFootstep = tryLoadSound(DIR_SFX + "footstep.wav");
  5. Find "public void playMenuOpen()" and add new method:
       public void playFootstep() { play(sfxFootstep, sfxVolume * 0.5f); }
  6. Find "disposeSound(sfxMenuOpen);" and add:
       disposeSound(sfxFootstep);

══════════════════════════════════════════════════════════════
  RECOMMENDED FREE SOUND SOURCES
══════════════════════════════════════════════════════════════
  https://freesound.org        (search: sword, jump, death, orb)
  https://opengameart.org      (RPG Sound Effects pack)
  https://pixabay.com/sound-effects

  Format: .wav for effects, .mp3 for music
  Convert at: https://online-audio-converter.com
══════════════════════════════════════════════════════════════
