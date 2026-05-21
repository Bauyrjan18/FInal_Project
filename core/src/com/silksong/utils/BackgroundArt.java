package com.silksong.utils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import java.util.Random;

/**
 * High-detail background art for all three locations.
 * Resolution: 320x180 (scaled 2.5x to 800x450 in rendering).
 * Each location has distinct visual identity.
 */
public class BackgroundArt {

    private static void p(Pixmap m, int x, int y, int rgba) {
        if (x<0||y<0||x>=m.getWidth()||y>=m.getHeight()) return;
        m.setColor(((rgba>>24)&0xFF)/255f,((rgba>>16)&0xFF)/255f,
                   ((rgba>>8)&0xFF)/255f,(rgba&0xFF)/255f);
        m.drawPixel(x,y);
    }
    private static void r(Pixmap m, int x, int y, int w, int h, int rgba) {
        if (w<=0||h<=0) return;
        m.setColor(((rgba>>24)&0xFF)/255f,((rgba>>16)&0xFF)/255f,
                   ((rgba>>8)&0xFF)/255f,(rgba&0xFF)/255f);
        m.fillRectangle(x,y,w,h);
    }
    private static void hline(Pixmap m, int x, int y, int len, int rgba) {
        for (int i=0;i<len;i++) p(m,x+i,y,rgba);
    }
    private static void vline(Pixmap m, int x, int y, int len, int rgba) {
        for (int i=0;i<len;i++) p(m,x,y+i,rgba);
    }

    // ══════════════════════════════════════════════════════════════
    // LOCATION 1: THE GATES  (Night street before the castle)
    // ══════════════════════════════════════════════════════════════
    public static Texture drawBgGates() {
        int W=320, H=180;
        Pixmap pm = new Pixmap(W, H, Pixmap.Format.RGBA8888);

        // ── Sky gradient ──
        for (int y=60; y<H; y++) {
            float t = (float)(y-60)/(H-60);
            int r=(int)(8+t*14), g=(int)(4+t*6), b=(int)(22+t*30);
            hline(pm, 0, H-1-y, W, (r<<24)|(g<<16)|(b<<8)|0xFF);
        }
        // Top sky darker
        r(pm, 0, 0, W, 60, 0x060412FF);

        // ── Stars ──
        Random rnd = new Random(7331);
        for (int i=0; i<80; i++) {
            int sx=rnd.nextInt(W), sy=rnd.nextInt(70);
            int bright = 120+rnd.nextInt(135);
            p(pm, sx, sy, (bright<<24)|(bright<<16)|(bright<<8)|0xFF);
            if (rnd.nextInt(3)==0) { // bigger star
                p(pm,sx+1,sy,0xFFFFFF88); p(pm,sx,sy+1,0xFFFFFF88);
            }
        }

        // ── Moon (top right) ──
        int mx=245, my=22, mr=18;
        for (int dy=-mr; dy<=mr; dy++)
            for (int dx=-mr; dx<=mr; dx++)
                if (dx*dx+dy*dy<=mr*mr)
                    p(pm,mx+dx,my+dy, 0xF0EECCFF);
        // Moon craters
        r(pm,mx+4,my+4,5,4, 0xD8D2AAFF);
        r(pm,mx-8,my+2,3,3, 0xD8D2AAFF);
        r(pm,mx+2,my-6,4,3, 0xD8D2AAFF);
        // Moon shadow edge
        for (int dy=-mr; dy<=mr; dy++)
            for (int dx=mr-4; dx<=mr; dx++)
                if (dx*dx+dy*dy<=mr*mr)
                    p(pm,mx+dx,my+dy, 0xB8B090CC);

        // ── Distant mountains silhouette ──
        int[] mh = new int[W];
        for (int x=0; x<W; x++) {
            double v = Math.sin(x*0.04)*14 + Math.sin(x*0.018)*20
                     + Math.sin(x*0.008)*28;
            mh[x] = 68 + (int)v;
        }
        for (int x=0; x<W; x++)
            vline(pm, x, H-mh[x]-20, mh[x]+20, 0x0E0A1EFF);

        // ── CASTLE — main structure ──
        int cx=110, cbase=H-18;
        // Castle base wall
        r(pm, cx-2,  cbase-80, 106, 80, 0x14101EFF);
        // Stone texture on wall
        for (int wy=cbase-78; wy<cbase-2; wy+=8) {
            for (int wx=cx; wx<cx+100; wx+=14) {
                int shade = ((wx/14+wy/8)%2==0) ? 0x1C1828FF : 0x18141EFF;
                r(pm, wx, wy, 12, 6, shade);
            }
        }

        // Left tower
        r(pm, cx-2, cbase-110, 26, 110, 0x10101AFF);
        // Tower battlements
        for (int bx=cx-2; bx<cx+24; bx+=8) {
            r(pm, bx, cbase-118, 5, 10, 0x10101AFF);
        }
        // Tower window (glowing yellow)
        r(pm, cx+6, cbase-90, 10, 14, 0x0A0810FF);
        r(pm, cx+7, cbase-89,  8, 12, 0x604010FF);
        r(pm, cx+8, cbase-88,  6, 10, 0xAA7020FF);
        r(pm, cx+9, cbase-87,  4,  8, 0xFFCC44FF); // glow

        // Right tower
        r(pm, cx+78, cbase-120, 28, 120, 0x0E0E18FF);
        for (int bx=cx+78; bx<cx+106; bx+=8) {
            r(pm, bx, cbase-128, 5, 10, 0x0E0E18FF);
        }
        // Tower window
        r(pm, cx+84, cbase-95, 10, 14, 0x0A0810FF);
        r(pm, cx+85, cbase-94,  8, 12, 0x604010FF);
        r(pm, cx+86, cbase-93,  6, 10, 0xAA7020FF);
        r(pm, cx+87, cbase-92,  4,  8, 0xFFCC44FF);

        // Central keep
        r(pm, cx+22, cbase-95, 58, 95, 0x12101CFF);
        for (int wy=cbase-93; wy<cbase-2; wy+=8) {
            for (int wx=cx+24; wx<cx+78; wx+=12) {
                int shade = ((wx/12+wy/8)%2==0) ? 0x1A1624FF : 0x16121EFF;
                r(pm, wx, wy, 10, 6, shade);
            }
        }

        // ── CASTLE GATE (main door) ──
        int gx=cx+38, gbase=cbase;
        // Door arch frame
        r(pm, gx, gbase-42, 26, 42, 0x0A0810FF);
        // Arch top (rounded)
        for (int ay=0; ay<13; ay++) {
            int hw = (int)(13.0 - Math.sqrt(Math.max(0, 13*13 - ay*ay)*(0.5)));
            r(pm, gx+hw, gbase-42-ay, 26-hw*2, 1, 0x0A0810FF);
        }
        // Door glow (purple magic)
        r(pm, gx+2, gbase-40, 22, 40, 0x2A0A44FF);
        r(pm, gx+4, gbase-38, 18, 36, 0x380A5AFF);
        r(pm, gx+6, gbase-36, 14, 32, 0x6622AAFF);
        // Door shine
        r(pm, gx+8, gbase-34, 5, 28, 0x9944CCFF);

        // Castle gate decorative arch keystone
        r(pm, gx+11, gbase-48, 5, 8, 0x221A30FF);
        // Iron portcullis bars
        for (int bx=gx+3; bx<gx+23; bx+=4) {
            r(pm, bx, gbase-39, 1, 38, 0x443355FF);
        }
        r(pm, gx+2, gbase-20, 22, 1, 0x443355FF); // horizontal bar

        // Gargoyles on top corners of gate
        r(pm, gx-4, gbase-52, 6, 10, 0x18141EFF); // left gargoyle
        r(pm, gx+25,gbase-52, 6, 10, 0x18141EFF); // right gargoyle

        // ── Castle rampart connection ──
        r(pm, cx+22, cbase-60, 16, 10, 0x14101EFF); // left connector
        r(pm, cx+64, cbase-60, 16, 10, 0x14101EFF); // right connector

        // ── Street / ground ──
        // Ground base — dark cobblestones
        r(pm, 0, cbase, W, H-cbase, 0x181210FF);
        // Cobblestone rows
        for (int gy=cbase; gy<H; gy+=6) {
            for (int gx2=0; gx2<W; gx2+=18) {
                int off = (gy/6%2)*9;
                int shade = ((gx2/18+gy/6)%3==0) ? 0x201A14FF
                          : ((gx2/18+gy/6)%3==1) ? 0x181410FF : 0x1C1812FF;
                r(pm, gx2+off, gy, 16, 5, shade);
                // Mortar lines
                r(pm, gx2+off+16, gy, 2, 5, 0x100C0AFF);
            }
            hline(pm, 0, gy-1, W, 0x100C0AFF);
        }

        // ── Puddles reflecting moonlight ──
        r(pm, 40, H-12, 28, 5, 0x1A1828FF);
        r(pm, 42, H-11, 24, 3, 0x2A2440FF);
        r(pm, 220, H-10, 22, 4, 0x1A1828FF);
        r(pm, 222, H-9, 18, 2, 0x2A2440FF);

        // ── Left wall with torch ──
        r(pm, 0, cbase-60, 18, 60, 0x12101AFF);
        // Wall stones
        for (int wy=cbase-58; wy<cbase; wy+=8) {
            r(pm, 1, wy, 16, 6, ((wy/8)%2==0)?0x181420FF:0x141018FF);
        }
        // Torch
        r(pm, 6, cbase-38, 5, 14, 0x6B4A1EFF); // handle
        r(pm, 5, cbase-46, 7, 10, 0x301408FF); // bowl
        // Flame (animated would need separate pass)
        r(pm, 5, cbase-52, 7, 8, 0xFF6600BB);
        r(pm, 6, cbase-56, 5, 6, 0xFF9900BB);
        r(pm, 7, cbase-58, 3, 4, 0xFFCC00BB);
        // Torch glow on wall
        for (int gy2=cbase-60; gy2<cbase-30; gy2++) {
            float gd = Math.abs(gy2-(cbase-46))/16f;
            int ga = (int)(60*(1-Math.min(1,gd)));
            hline(pm, 0, gy2, Math.max(0,(int)(18*(1-gd))), 0xFF8800<<8|ga);
        }

        // ── Right wall ──
        r(pm, W-18, cbase-60, 18, 60, 0x12101AFF);
        for (int wy=cbase-58; wy<cbase; wy+=8) {
            r(pm, W-17, wy, 16, 6, ((wy/8)%2==0)?0x181420FF:0x141018FF);
        }
        // Torch right
        r(pm, W-12, cbase-38, 5, 14, 0x6B4A1EFF);
        r(pm, W-13, cbase-46, 7, 10, 0x301408FF);
        r(pm, W-13, cbase-52, 7, 8, 0xFF6600BB);
        r(pm, W-12, cbase-56, 5, 6, 0xFF9900BB);
        r(pm, W-11, cbase-58, 3, 4, 0xFFCC00BB);

        // ── Fog layer near ground ──
        for (int fy=0; fy<12; fy++) {
            int fa = (int)(35*(1-fy/12f));
            hline(pm, 0, H-1-fy, W, 0x0A0818<<8|fa);
        }

        // ── Ground line highlight ──
        hline(pm, 0, cbase-1, W, 0x302838FF);

        Texture t = new Texture(pm); pm.dispose();
        t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        return t;
    }

    // ══════════════════════════════════════════════════════════════
    // LOCATION 2: THE GREAT HALL (Inside the castle)
    // ══════════════════════════════════════════════════════════════
    public static Texture drawBgGreatHall() {
        int W=320, H=180;
        Pixmap pm = new Pixmap(W, H, Pixmap.Format.RGBA8888);

        // ── Ceiling (vaulted stone) ──
        r(pm, 0, H-H, W, 40, 0x0C0A18FF);
        // Vault ribs across ceiling
        for (int vx=40; vx<W; vx+=60) {
            // Diagonal rib left side
            for (int vy=0; vy<30; vy++) {
                p(pm, vx-vy/2, vy, 0x2A2240FF);
                p(pm, vx-vy/2+1, vy, 0x1E1A30FF);
            }
            // Diagonal rib right side
            for (int vy=0; vy<30; vy++) {
                p(pm, vx+vy/2, vy, 0x2A2240FF);
                p(pm, vx+vy/2+1, vy, 0x1E1A30FF);
            }
        }

        // ── Back wall with arched windows ──
        r(pm, 0, 35, W, 95, 0x100E1CFF);
        // Stone blocks on back wall
        for (int wy=36; wy<128; wy+=10) {
            for (int wx=0; wx<W; wx+=20) {
                int off = (wy/10%2)*10;
                int shade = ((wx/20)%2==0)?0x161220FF:0x121028FF; // alternate
                r(pm, wx+off, wy, 18, 8, shade);
                hline(pm, wx+off, wy-1, 18, 0x0C0A14FF);
                vline(pm, wx+off-1, wy, 8, 0x0C0A14FF);
            }
        }

        // ── Arched stained windows (3 big ones) ──
        int[] winX = {35, 140, 245};
        for (int wx : winX) {
            // Window arch frame (stone)
            r(pm, wx, 42, 40, 55, 0x1A1428FF);
            // Arch top
            for (int ay=0; ay<20; ay++) {
                int hw = (int)(20 - Math.sqrt(Math.max(0,400-ay*ay)));
                if (hw < 20)
                    r(pm, wx+hw, 42+19-ay, 40-hw*2, 1, 0x1A1428FF);
            }
            // Window glass (purple-blue glow)
            r(pm, wx+4, 48, 32, 44, 0x0A0818FF);
            // Purple upper pane
            r(pm, wx+6, 50, 28, 20, 0x3A1A5AFF);
            r(pm, wx+8, 52, 24, 16, 0x5A2A7AFF);
            // Cross divider
            r(pm, wx+18, 50, 4, 42, 0x1A1428FF);
            r(pm, wx+6, 68, 28, 4, 0x1A1428FF);
            // Lower pane (moonlight blue)
            r(pm, wx+6, 72, 12, 18, 0x1A2A5AFF);
            r(pm, wx+22, 72, 12, 18, 0x1A1A4AFF);
            // Window glow on floor
            for (int gy=128; gy<H-18; gy++) {
                float gd = (gy-128)/40f;
                int ga = (int)(40*(1-Math.min(1,gd)));
                int gw = (int)(40*(1-gd*0.5f));
                r(pm, wx+(40-gw)/2, gy, gw, 1, 0x4A2A6A<<8|ga);
            }
        }

        // ── Massive pillars ──
        int[] pilX = {0, 88, 178, 270};
        for (int px : pilX) {
            // Pillar base (wider)
            r(pm, px, H-26, 24, 26, 0x1A1628FF);
            r(pm, px-2, H-30, 28, 6, 0x221E34FF);
            // Pillar shaft
            r(pm, px+2, 55, 20, H-81, 0x161220FF);
            // Pillar highlight
            r(pm, px+3, 55, 3, H-81, 0x221E30FF);
            // Pillar top capital
            r(pm, px-2, 50, 28, 10, 0x201C2EFF);
            r(pm, px-4, 50, 32, 5, 0x282438FF);
            // Carved skull on capital
            r(pm, px+8, 52, 8, 7, 0x1A1424FF);
            p(pm, px+10, 54, 0xFF2200BB); p(pm, px+14, 54, 0xFF2200BB);
            // Torch bracket on pillar
            if (px > 0 && px < W-24) {
                r(pm, px+20, 80, 6, 14, 0x553322FF);
                r(pm, px+19, 74, 8, 8, 0x443322FF);
                r(pm, px+19, 68, 8, 8, 0xFF5500AA);
                r(pm, px+20, 64, 6, 6, 0xFF8800AA);
                r(pm, px+21, 61, 4, 5, 0xFFCC00AA);
                // Glow on pillar
                for (int gy=60; gy<100; gy++) {
                    float gd = Math.abs(gy-76)/20f;
                    int ga = (int)(50*(1-Math.min(1,gd)));
                    r(pm, px+2, gy, 22, 1, 0xFF6600<<8|ga);
                }
            }
        }

        // ── Floor ──
        r(pm, 0, H-22, W, 22, 0x14101EFF);
        // Floor tiles (large dark stone)
        for (int fy=H-22; fy<H; fy+=11) {
            for (int fx=0; fx<W; fx+=22) {
                int shade = ((fx/22+fy/11)%2==0) ? 0x1C1828FF : 0x181422FF;
                r(pm, fx+1, fy+1, 20, 9, shade);
            }
        }
        // Floor highlight line
        hline(pm, 0, H-23, W, 0x2A2238FF);

        // ── Hanging chains / chandeliers ──
        for (int hx=80; hx<W-80; hx+=80) {
            // Chain links
            for (int hy=4; hy<28; hy+=4) {
                r(pm, hx-1, hy, 3, 2, 0x445566FF);
                r(pm, hx, hy+2, 2, 2, 0x334455FF);
            }
            // Candle holder
            r(pm, hx-8, 28, 18, 4, 0x334455FF);
            // Candles
            for (int cy=0; cy<3; cy++) {
                int cx2 = hx-6+cy*6;
                r(pm, cx2, 24, 3, 5, 0xEEDDCCFF);
                r(pm, cx2, 22, 3, 3, 0xFF8800AA);
                r(pm, cx2+1, 20, 1, 3, 0xFFCC00AA);
            }
        }

        // ── Blood stains on floor ──
        r(pm, 60, H-18, 14, 6, 0x380808FF);
        r(pm, 63, H-16, 8, 3, 0x4A0A0AFF);
        r(pm, 190, H-20, 10, 5, 0x380808FF);

        // ── Skull pile in far corner ──
        for (int si=0; si<4; si++) {
            r(pm, 4+si*6, H-25-si*3, 10, 8, 0xC8BCB0FF);
            p(pm, 6+si*6, H-22-si*3, 0x660000FF);
            p(pm, 10+si*6, H-22-si*3, 0x660000FF);
        }

        // ── Fog near floor ──
        for (int fy=0; fy<8; fy++) {
            int fa = (int)(30*(1-fy/8f));
            hline(pm, 0, H-1-fy, W, 0x100820<<8|fa);
        }

        Texture t = new Texture(pm); pm.dispose();
        t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        return t;
    }

    // ══════════════════════════════════════════════════════════════
    // LOCATION 3: THRONE ROOM (Boss arena)
    // ══════════════════════════════════════════════════════════════
    public static Texture drawBgThroneRoom() {
        int W=320, H=180;
        Pixmap pm = new Pixmap(W, H, Pixmap.Format.RGBA8888);

        // ── Deep red stone ceiling ──
        r(pm, 0, 0, W, 45, 0x100808FF);
        // Ceiling carved panels
        for (int cx=20; cx<W-20; cx+=40) {
            r(pm, cx, 5, 30, 30, 0x150A0AFF);
            r(pm, cx+2, 7, 26, 26, 0x180C0CFF);
            // Rune glyph in panel
            r(pm, cx+12, 12, 6, 2, 0x660000FF);
            r(pm, cx+14, 10, 2, 6, 0x660000FF);
            r(pm, cx+10, 16, 4, 2, 0x440000FF);
            r(pm, cx+16, 16, 4, 2, 0x440000FF);
        }

        // ── Massive back wall ──
        r(pm, 0, 42, W, 100, 0x0E0808FF);
        // Red stone blocks
        for (int wy=43; wy<140; wy+=12) {
            for (int wx=0; wx<W; wx+=24) {
                int off = (wy/12%2)*12;
                int shade = ((wx/24+wy/12)%2==0) ? 0x160A0AFF : 0x120808FF;
                r(pm, wx+off, wy, 22, 10, shade);
                hline(pm, wx+off, wy-1, 22, 0x0A0404FF);
                vline(pm, wx+off-1, wy, 10, 0x0A0404FF);
            }
        }

        // ── MASSIVE THRONE (center back) ──
        int tx=110, tbase=H-22;
        // Throne base steps
        r(pm, tx-10, tbase-8, 120, 8, 0x1C0C0CFF);
        r(pm, tx-6, tbase-14, 112, 8, 0x1A0A0AFF);
        r(pm, tx, tbase-18, 100, 8, 0x200E0EFF);
        hline(pm, tx-10, tbase-9, 120, 0x3A1818FF);
        hline(pm, tx-6, tbase-15, 112, 0x341414FF);

        // Throne seat
        r(pm, tx+10, tbase-30, 80, 14, 0x1E0C0CFF);
        r(pm, tx+12, tbase-32, 76, 4, 0x2A1010FF);
        // Armrests
        r(pm, tx+8, tbase-44, 12, 28, 0x1A0808FF);
        r(pm, tx+80, tbase-44, 12, 28, 0x1A0808FF);
        r(pm, tx+6, tbase-46, 16, 4, 0x2A1010FF);
        r(pm, tx+78, tbase-46, 16, 4, 0x2A1010FF);

        // Throne back (huge)
        r(pm, tx+8, tbase-90, 84, 62, 0x180808FF);
        r(pm, tx+10, tbase-92, 80, 4, 0x221010FF);
        // Throne arch top
        for (int ay=0; ay<25; ay++) {
            int hw = (int)(42 - Math.sqrt(Math.max(0,42.0*42-ay*ay)));
            r(pm, tx+8+hw, tbase-90-ay, 84-hw*2, 1, 0x180808FF);
        }
        // Throne decorative line
        hline(pm, tx+8, tbase-90, 84, 0x4A1818FF);
        for (int dx=0; dx<84; dx+=8) {
            r(pm, tx+9+dx, tbase-94, 5, 6, 0x3A1414FF); // battlements
        }

        // Throne gold trim
        hline(pm, tx+8, tbase-88, 84, 0xAA6600FF);
        hline(pm, tx+8, tbase-52, 84, 0xAA6600FF);
        vline(pm, tx+8, tbase-88, 36, 0xAA6600FF);
        vline(pm, tx+91, tbase-88, 36, 0xAA6600FF);

        // Throne skull decoration (top)
        r(pm, tx+38, tbase-105, 24, 16, 0xC0B4A0FF);
        r(pm, tx+42, tbase-108, 16, 4, 0xC0B4A0FF);
        // Skull eyes
        r(pm, tx+42, tbase-102, 5, 5, 0x0A0404FF);
        r(pm, tx+53, tbase-102, 5, 5, 0x0A0404FF);
        r(pm, tx+44, tbase-100, 3, 3, 0xFF0000FF); // glowing red
        r(pm, tx+55, tbase-100, 3, 3, 0xFF0000FF);
        // Jaw
        r(pm, tx+42, tbase-92, 16, 3, 0xA0948CFF);
        for (int tooth=0; tooth<5; tooth++) {
            r(pm, tx+43+tooth*3, tbase-90, 2, 4, 0xD4C8BCFF);
        }

        // ── Cracked pillars (battle-worn) ──
        int[] cpx = {12, W-36};
        for (int px : cpx) {
            r(pm, px, H-26, 24, 26, 0x1E0E0EFF);
            r(pm, px-2, H-30, 28, 6, 0x2A1414FF);
            r(pm, px+2, 42, 20, H-68, 0x180C0CFF);
            r(pm, px+3, 42, 3, H-68, 0x221212FF);
            r(pm, px-2, 38, 28, 8, 0x201010FF);
            r(pm, px-4, 38, 32, 4, 0x2A1818FF);
            // CRACKS
            for (int cy=50; cy<H-40; cy+=20) {
                p(pm, px+8, cy, 0x0A0404FF);
                p(pm, px+9, cy+1, 0x0A0404FF);
                p(pm, px+8, cy+2, 0x0A0404FF);
                p(pm, px+10, cy+3, 0x0A0404FF);
            }
            // Red torches on pillars
            r(pm, px+20, 70, 8, 16, 0x5A3010FF);
            r(pm, px+19, 62, 10, 10, 0x3A1808FF);
            r(pm, px+19, 54, 10, 10, 0xFF3300CC);
            r(pm, px+20, 48, 8, 8, 0xFF5500CC);
            r(pm, px+21, 43, 6, 7, 0xFF8800CC);
            // Torch glow
            for (int gy=44; gy<H-40; gy++) {
                float gd = Math.abs(gy-66)/26f;
                int ga = (int)(80*(1-Math.min(1,gd)));
                r(pm, px, gy, 24, 1, 0xFF2200<<8|ga);
            }
        }

        // ── Magic circles on floor ──
        r(pm, 0, H-22, W, 22, 0x120808FF);
        // Large central circle
        int mccx=W/2, mccy=H-12, mcr=40;
        for (int dy=-mcr; dy<=mcr; dy++) {
            for (int dx=-mcr; dx<=mcr; dx++) {
                int d2=dx*dx+dy*dy;
                if (d2 >= (mcr-2)*(mcr-2) && d2 <= mcr*mcr)
                    p(pm, mccx+dx, mccy+dy, 0x880000FF);
                if (d2 >= (mcr-8)*(mcr-8) && d2 <= (mcr-6)*(mcr-6))
                    p(pm, mccx+dx, mccy+dy, 0xAA0000FF);
            }
        }
        // Inner rune lines
        hline(pm, mccx-30, mccy, 60, 0x660000FF);
        vline(pm, mccx, mccy-8, 16, 0x660000FF);
        for (int ri=0; ri<6; ri++) {
            double angle = ri * Math.PI / 3;
            for (int rd=10; rd<38; rd++) {
                int rx=(int)(mccx+rd*Math.cos(angle));
                int ry=(int)(mccy+rd*Math.sin(angle)*0.3f);
                p(pm, rx, ry, 0x550000FF);
            }
        }

        // Floor tiles
        for (int fy=H-22; fy<H; fy+=11) {
            for (int fx=0; fx<W; fx+=22) {
                int shade = ((fx/22+fy/11)%2==0) ? 0x1C0E0EFF : 0x180A0AFF;
                r(pm, fx+1, fy+1, 20, 9, shade);
            }
        }
        hline(pm, 0, H-23, W, 0x381818FF);

        // ── Lava crack on floor (atmospheric) ──
        for (int lx=60; lx<90; lx++) {
            p(pm, lx, H-8, 0xFF2200FF);
            p(pm, lx, H-7, 0xFF4400AA);
        }
        for (int lx=200; lx<230; lx++) {
            p(pm, lx, H-9, 0xFF2200FF);
            p(pm, lx, H-8, 0xFF4400AA);
        }

        // ── Red atmospheric fog ──
        for (int fy=0; fy<10; fy++) {
            int fa = (int)(40*(1-fy/10f));
            hline(pm, 0, H-1-fy, W, 0x220808<<8|fa);
        }

        Texture t = new Texture(pm); pm.dispose();
        t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        return t;
    }
}
