/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hungerbow;



/**
 *
 * @author Cloud
 */
public class Panel extends javax.swing.JComponent implements java.awt.event.KeyListener, java.awt.event.MouseMotionListener, java.awt.event.MouseListener {
    
    public void keyTyped(java.awt.event.KeyEvent e){}
    public void keyPressed(java.awt.event.KeyEvent e) {
        switch(e.getKeyCode()){
            case 32:
            case 10:
                if(isGameOver || gameFinished){
                    if(e.getKeyCode() == 32 || e.getKeyCode() == 10){
                    if(gameFinished) gameFinished = !gameFinished;
                       restart();
                    }else if(e.getKeyCode() == 27){
                        System.exit(0);
                    }
                    //Untuk Set Tembakan Keyboard Panah Awal Ke Target
                } else if(isAlowFire) {
                  playFire();
                  panah.isVisible = !panah.isVisible;
                  panah.x = pemanah.x;
                  panah.y = pemanah.y + 35;
                  isAlowFire = !panah.isVisible;
                }
                break;
            case 27:
                System.exit(0);
                break;
            case 65:
                startNextLevel();
                break;
        }
    }
    
     public void keyReleased(java.awt.event.KeyEvent e) {}
     
     @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        y = e.getY();
        if(y > minYPemanah && y < maxYPemanah)
            pemanah.y = e.getY();
            meja.y = pemanah.y + 127;
            pegas.y = pemanah.y + 92;
    }
    int y;
    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
        y = e.getY();
        if(y > minYPemanah && y < maxYPemanah)
            pemanah.y = e.getY();
            meja.y = pemanah.y + 127;
            pegas.y = pemanah.y + 92;
    }
    
        @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {}

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        //Untuk Set Tembakan Klik Mouse Panah Awal Ke Target
        if(isAlowFire && kesempatanCount > 0){
            playFire();
            panah.isVisible = !panah.isVisible;
            panah.x = pemanah.x;
            panah.y = pemanah.y + 35;
            isAlowFire = !panah.isVisible;
        }
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {}
    

    public enum TypeScreen{ Running, Paused, GameOver };
    private TypeScreen type;
    
    public TypeScreen getType() {
        return type;
    }
    
    
    boolean gameFinished;
    int minYPemanah, maxYPemanah;
    int minYTarget, maxYTarget;
    int minXSnag, maxXSnag, minYSnag, maxYSnag;
    final int MAXGUNS = 5;
    int widthSnag = 25, heightSnag = 25;
    int requestPoints = 100;
    javax.sound.sampled.Clip fireClip, hitClip, reloadClip;
    javax.sound.midi.Sequencer sequencerLose;
    java.awt.Font textFont, boldFont;
    java.util.Timer timer;
    GameEntry papanGame, target, pemanah, panah, kesempatan, points, gambarAtas, tiang, meja, pegas;
    GameEntry showNewPoints;
    GameEntry gameNotRun, GameRun;
    java.util.ArrayList<GameEntry> snags;
    int level = 1;
    boolean isAlowFire, isGameOver;
    int kesempatanCount;
    int intGuns;
    java.awt.Color textColor, rectColor, backColor, pointsColor, topColor, mColor;
    String strM = "", strM2 = "";
    int gamePoints, kesempatanRange;
    int pointsAlpha;
    int timerSnags, snagTime, timeAddSnag;
    java.util.Random rnd;
    boolean hasSnags;
    readPoints readPoints;
    boolean isChecked;
    boolean isNewState;
    boolean newGun;
    
    public Panel(){
        setFocusable(true);
        addKeyListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
        
        rnd = new java.util.Random();
        
        readPoints = new readPoints();
        
        javax.sound.midi.Sequence sequence = null;
        javax.sound.midi.Sequencer sequencer = null;
        
        try {
            sequence = javax.sound.midi.MidiSystem.getSequence(getClass().getResource("suara/music.mid"));
            sequencer = javax.sound.midi.MidiSystem.getSequencer();
            sequencer.setLoopCount(-1);
            sequencer.open();
            sequencer.setSequence(sequence);
        } catch (Exception ex) {}
        sequencer.start();
        
        try {
            javax.sound.sampled.AudioInputStream audio = javax.sound.sampled.AudioSystem.getAudioInputStream(getClass().getResource("suara/fire.wav"));
            fireClip = javax.sound.sampled.AudioSystem.getClip();
            fireClip.open(audio);
            audio = javax.sound.sampled.AudioSystem.getAudioInputStream(getClass().getResource("suara/hit.wav"));
            hitClip = javax.sound.sampled.AudioSystem.getClip();
            hitClip.open(audio);
            audio = javax.sound.sampled.AudioSystem.getAudioInputStream(getClass().getResource("suara/reload.wav"));
            reloadClip = javax.sound.sampled.AudioSystem.getClip();
            reloadClip.open(audio);
        } catch (Exception ex) {}
        
        try {
            sequence = javax.sound.midi.MidiSystem.getSequence(getClass().getResource("suara/lose.mid"));
            sequencerLose = javax.sound.midi.MidiSystem.getSequencer();
            sequencerLose.open();
            sequencerLose.setSequence(sequence);
        } catch (Exception ex) {
        }
        
        snagTime = 100;
        isAlowFire = true;
        kesempatanCount = 3;
        backColor = java.awt.Color.darkGray;
        textColor = java.awt.Color.white;
        rectColor = new java.awt.Color(27, 7, 10);
        topColor = new java.awt.Color(0, 0, 191, 255);
        mColor = new java.awt.Color(0, 62, 93, 170);
        pointsColor = java.awt.Color.red;
        
        textFont = new java.awt.Font("Arial", java.awt.Font.BOLD, 20);
        boldFont = new java.awt.Font("Arial", java.awt.Font.BOLD, 11);
        kesempatanRange = 50;
        
        showNewPoints = new GameEntry(0,0,50,50);
        showNewPoints.isVisible = false;
        showNewPoints.speed = 2;
        
        papanGame = new GameEntry();
        papanGame.img = new javax.swing.ImageIcon(getClass().getResource("background/bglevel1.jpg")).getImage();
        papanGame.x = 0;
        papanGame.y = 150;
        papanGame.width = 820;
        papanGame.height = 465;
        
        //jarak poin target
        minYPemanah = papanGame.y + 55;
        maxYPemanah = papanGame.y + papanGame.height - 180;
        
        minYTarget = papanGame.y + 55;
        maxYTarget = papanGame.y + papanGame.height - 180;
        
        
        minXSnag = papanGame.x + 170;
        minYSnag = papanGame.y - 10;
        maxXSnag = papanGame.x + 600;
        maxYSnag = papanGame.y + papanGame.height + 100;
        
        snags = new java.util.ArrayList();
        
        gameNotRun = new GameEntry((papanGame.x + papanGame.width / 2) - (294 / 2), (papanGame.y + papanGame.height / 2) - (250 / 2), 294, 250);
        gameNotRun.img = new javax.swing.ImageIcon(getClass().getResource("asset/overpause.png")).getImage();
        
        gambarAtas = new GameEntry(0,0,papanGame.width, 160);
        gambarAtas.img = new javax.swing.ImageIcon(getClass().getResource("asset/top.png")).getImage();
        
        kesempatan = new GameEntry();
        kesempatan.img = new javax.swing.ImageIcon(getClass().getResource("asset/placewood.png")).getImage();
        kesempatan.width = 180;
        kesempatan.height = 42;
        kesempatan.x = papanGame.x + papanGame.width - kesempatan.width;
        kesempatan.y = papanGame.y + 5;
        
        points = new GameEntry();
        points.img = new javax.swing.ImageIcon(getClass().getResource("asset/placewood.png")).getImage();
        points.width = kesempatan.width - 20;
        points.height = kesempatan.height;
        points.x = papanGame.x + 5;
        points.y = kesempatan.y;
        
        pemanah = new GameEntry();
        pemanah.img = new javax.swing.ImageIcon(getClass().getResource("character/pemanah.png")).getImage();
        pemanah.speed = 0;
        pemanah.width = 120;
        pemanah.height = 135;
        pemanah.x = papanGame.width - pemanah.width - 10;
        pemanah.y = minYPemanah;
        
        meja = new GameEntry();
        meja.img = new javax.swing.ImageIcon(getClass().getResource("character/meja.png")).getImage();
        meja.speed = 0;
        meja.width = 60;
        meja.height = 20;
        meja.x = pemanah.x + 45;
        meja.y = pemanah.y + 127;
        
        pegas = new GameEntry();
        pegas.img = new javax.swing.ImageIcon(getClass().getResource("character/pegas.png")).getImage();
        pegas.speed = 0;
        pegas.width = 60;
        pegas.height = papanGame.height;
        pegas.x = meja.x ;
        pegas.y = pemanah.y + 92;
        
        target = new GameEntry();
        target.img = new javax.swing.ImageIcon(getClass().getResource("character/target_ban.png")).getImage();
        target.speed = 1;
        target.width = 95;
        target.height = 130;
        target.x = papanGame.x + 5;
        target.y = minYTarget;
        
        tiang = new GameEntry();
        tiang.img = new javax.swing.ImageIcon(getClass().getResource("character/tiang1.png")).getImage();
        tiang.speed = 0;
        tiang.width =95;
        tiang.height = papanGame.height;
        tiang.x = papanGame.x + 5;
        tiang.y = minYTarget + 3;
                
        panah = new GameEntry(0,0,50,30);
        panah.img = new javax.swing.ImageIcon(getClass().getResource("character/panah.png")).getImage();
        panah.speed = -10;
        panah.isVisible = false;
        
        timeAddSnag = 15;
        
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
        }
        
        timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(), 100, 15);
    }


    private class GameEntry {
        java.awt.Image img;
        int x,y, width,height, speed;
        boolean isVisible, isGun;
        String text;
        
        public GameEntry() {
            isVisible = true;
        }
        
        public GameEntry(int x, int y, int width, int height) {
            isVisible = true;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            isVisible = true;
        }
        
        public GameEntry(int x, int y, int width, int height, java.awt.Image img){
            isVisible = true;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
            isVisible = true;
        }
        
        public GameEntry(int x, int y, int width, int height, int speed, java.awt.Image img){
            isVisible = true;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.speed = speed;
            this.img = img;
            isVisible = true;
            isGun = newGun;
            newGun = false;
        }
        
    }
    
    private class readPoints {
    
        private int[] codes = {101,202,501,240,332,42,111,555,444,511,55};
        
        private int index;
        private String f;
        
        public readPoints() {
            f = "spm.dat";
        }
        
        public int getPoints() {
            int intReturn = 0;
            try {
                int read = 0;
                String str = "";
                java.io.FileReader fr = new java.io.FileReader(f);
                while((read = fr.read()) != -1)
                    str += read;
                for(int i = 0; i < str.length(); i++){
                    if(index >= codes.length) index = 0;
                    intReturn += (int)(str.charAt(i) + codes[index]);
                    index++;
                }
                index = 0;
                fr.close();
            } catch (Exception e) {
            }
            return intReturn;
        }
        
        public void setPoints(int value){
        try {
            java.io.FileWriter fw = new java.io.FileWriter(f);
            String strValue = "" + value;
            int b = 0;
            for(int i = 0; i < strValue.length();i++){
                if(index > codes.length) index++;
                b = (int) (strValue.charAt(i) - codes[index]);
                fw.write(b);
                index++;
            }
            index = 0;
            fw.close();
        } catch (Exception ex) {
        }
    }
        
    }
    
    public javax.swing.JComponent getparent() {
        return this;
    }
    
    private int checkedLevel;
    private void addPoints(int posY1, int posY2){
        int distance = 0, p = 0;
        
        if(posY1 > posY2 + (target.height / 2)){
            distance = posY1 - (posY2 + (target.height / 2));
        }
        
        p += 60;
        gamePoints += p;
        
        if(gamePoints >= kesempatanRange) {
            kesempatanCount++;
            kesempatanRange += 50;
        }
        
        if(gamePoints >= kesempatanRange) {
            kesempatanCount++;
            kesempatanRange += 50;
        }
        
        if(gamePoints >= requestPoints){
            requestPoints += 100;
            startNextLevel();
            if(level == 4 || level == 7) checkedLevel = level;
        }
        
        if(target.speed > 0)
            showNewPoints.speed = 1;
        else
            showNewPoints.speed = 2;
        pointsAlpha = 255;
        showNewPoints.text = "" + p;
        panah.isVisible = !isAlowFire;
        showNewPoints.isVisible = !showNewPoints.isVisible;
    }
    
    private void playFire() {
            fireClip.setFramePosition(0);
            fireClip.start();
    }
    
    private void playReload() {
            reloadClip.setFramePosition(0);
            reloadClip.start();
    }
    
    private void playHit() {
        hitClip.setFramePosition(0);
        hitClip.start();
    }
    
    private void playMid() {
        sequencerLose.setTickPosition(0);
        sequencerLose.start();
    }
    
    private class ScheduleTask extends java.util.TimerTask {
        
        @Override
        public void run() {
            if(gameFinished) {
            strM = "Selesai";
            isNewState = true;
            restart();
            return;
            }else if(kesempatanCount < 1) {
                if(!isGameOver) playMid();
                isGameOver = true;
                strM = "Ingin Bermain Lagi ?";
                strM2 = "Tekan Enter untuk Restart     Tekan Esc untuk Keluar";
            }else{
                if(hasSnags) {
                    if(timerSnags >= snagTime)
                        snagTime = 400 - (level * 75);
                    if(snagTime < 0)
                        snagTime = 100;
                    snagTime = rnd.nextInt(snagTime);
                    if(snagTime < 75) {
                        snagTime += 100;
                    }
                    timerSnags = 0;
                    int xSnag = rnd.nextInt(maxXSnag);
                    if(xSnag < minXSnag)
                        xSnag = xSnag + (minXSnag - xSnag) + rnd.nextInt(100);
                   int ySnag = rnd.nextInt(minYSnag);
                   int imgSnag = rnd.nextInt(5);
                   if(imgSnag < 1) imgSnag = 1;
                   else if(imgSnag > 4) imgSnag =4;
                   if(newGun&&rnd.nextInt(minYSnag) == minYSnag-1){
                       intGuns ++;
                       if(intGuns > MAXGUNS) intGuns = rnd.nextInt(MAXGUNS);
                   }
                       timerSnags++;
                   }
                if(!isNewState && (pemanah.y < minYPemanah || pemanah.y > maxYPemanah))
                    pemanah.speed = pemanah.speed * -1;
                }
                if(target.y < minYTarget || target.y > maxYTarget)
                    target.speed = target.speed * -1;
                if(!isAlowFire){
                    if((panah.x + panah.speed < target.x + target.width / 2 + 7 && panah.x > (target.x + 10) + target.width / 2 - 20) && (panah.y + panah.height / 2 >= target.y && panah.y + panah.height / 2 <= target.y + target.height)){
                        isAlowFire = true;
                        showNewPoints.x = panah.x;
                        showNewPoints.y = target.y;
                        addPoints(panah.y + panah.height / 2, target.y);
                        playHit();
                    }else if(panah.x < -100){
                        isAlowFire = true;
                        panah.isVisible = !isAlowFire;
                        kesempatanCount--;
                        playReload();
                    }
                    else
                        panah.x += panah.speed;
                }
                if(!isNewState) pemanah.y += pemanah.speed;
                target.y += target.speed;
                
                if(showNewPoints.isVisible){
                    if(pointsAlpha < 1){
                        showNewPoints.isVisible = !showNewPoints.isVisible;
                    }else{
                        pointsColor = new java.awt.Color(pointsColor.getRed(), pointsColor.getGreen(), pointsColor.getBlue(), pointsAlpha);
                        showNewPoints.y -= showNewPoints.speed;
                        pointsAlpha -= 7;
                    }
                }
                repaint();
            }
        }

    
    public void restart() {
        if(gameFinished){
        gameFinished = false;
        isChecked = false;
        }
        kesempatanCount = 3;
        isGameOver = false;
        level = 1;
        target.speed = 1;
        if(checkedLevel == 4 || checkedLevel == 7) isChecked = true;
        else {
            gamePoints = 0;
            requestPoints = 100;
        }
        panah.speed = -10;
        hasSnags = false;
        timeAddSnag = 15;
        papanGame.img = new javax.swing.ImageIcon(getClass().getResource("background/bglevel1.jpg")).getImage();
        snags.clear();
        widthSnag = 25;
        heightSnag = 25;
        kesempatanRange = 50;
        pointsColor = java.awt.Color.red;
        
        if(isChecked) {
            for(int i = 0; i < checkedLevel - 1; i++){
            startNextLevel();
                try {
                    Thread.sleep(25);
                } catch (Exception ex) {
                }
            }
        }
    }
    
    private void startNextLevel() {
        level++;
        if(level > 8) {
            isNewState = true;
            strM = "Selamat, Kamu Sudah Menang!";
            strM2 = "Kamu Sudah Sejauh Ini, Kerja Bagus!";
            pointsColor = java.awt.Color.red;
        }else {
            if(level > 2) {
                timeAddSnag--;
                hasSnags = true;
                if(level > 3) {
                    widthSnag = 35;
                    heightSnag = 35;
                }
            }else if(level == 8){
                hasSnags = false;
            }
            
            
            if(level == 2)
                pointsColor = java.awt.Color.black;
            else if(level == 3)
                pointsColor = java.awt.Color.cyan;
            else if(level == 4)
                pointsColor = java.awt.Color.white;
            else if(level == 5)
                pointsColor = java.awt.Color.white;
            else if(level == 6)
                pointsColor = java.awt.Color.red;
            else if(level == 7)
                pointsColor = java.awt.Color.black;
            
            
            papanGame.img = new javax.swing.ImageIcon(getClass().getResource("background/bglevel" + level + ".jpg")).getImage();
            kesempatanCount++;
            kesempatanRange -= (level * 25) + (kesempatanRange % 50) * 2;
            panah.speed -= 2;
            target.speed = level;
        }
    }
   
 
    @Override
    protected void paintComponent(java.awt.Graphics g){
    g.setColor(backColor);
    g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
    g.setFont(boldFont);
    g.setColor(java.awt.Color.black);
    g.drawImage(papanGame.img, papanGame.x, papanGame.y, papanGame.width, papanGame.height, this);
    if(hasSnags) {
    for(int i=0; i < snags.size(); i++){
        g.drawImage(snags.get(i).img, snags.get(i).x, snags.get(i).y, snags.get(i).width, snags.get(i).height,this);
        }
        }
    g.drawImage(gambarAtas.img, gambarAtas.x, gambarAtas.y, gambarAtas.width, gambarAtas.height, this);
    g.setColor(topColor);
    
    //Petak di poin
    g.fillRect(papanGame.x, papanGame.y, papanGame.width, 55);
    
    g.setFont(textFont);
    g.setColor(textColor);
    g.drawString("Level : " + level + " | ", 180, papanGame.y + 35);
    g.drawString("Next Level : " + requestPoints + " | ", 300 , papanGame.y + 35);
    
    //best poin hunger bow
    g.drawString("Amunisi : " + kesempatanCount, 510, papanGame.y + 35);

    g.drawImage(points.img, points.x, points.y, points.width, points.height, this);
    g.setColor(textColor);
    g.drawString("Poin : " + gamePoints, points.x + 25, points.y + 28);
    g.drawImage(kesempatan.img, kesempatan.x, kesempatan.y, kesempatan.width, kesempatan.height, this);
    for(int i = 0; i < kesempatanCount; i++)
        g.drawImage(target.img, kesempatan.x + (i * (kesempatan.width / kesempatanCount)) + 5, kesempatan.y + 5, 18, 32, this);
        
    g.setColor(rectColor);
    
    g.drawImage(meja.img, meja.x, meja.y, meja.width, meja.height, this);
    g.drawImage(pegas.img, pegas.x, pegas.y, pegas.width, pegas.height, this);
    g.drawImage(pemanah.img, pemanah.x, pemanah.y, pemanah.width, pemanah.height,this);
    g.drawImage(tiang.img, tiang.x, tiang.y, tiang.width, tiang.height,  this);
    g.drawImage(target.img, target.x, target.y, target.width, target.height, this);
    if(panah.isVisible)
        g.drawImage(panah.img, panah.x, panah.y, panah.width, panah.height, this);
    if(showNewPoints.isVisible){
        g.setColor(pointsColor);
        g.drawString(showNewPoints.text, showNewPoints.x, showNewPoints.y);
    }
    if(isGameOver || gameFinished || isNewState){
        g.setColor(mColor);
        g.fillRect(papanGame.x, papanGame.y, papanGame.width, papanGame.height);
        g.setColor(textColor);
        g.drawString(strM, papanGame.x + papanGame.width / 2 - (strM.length() * 5), papanGame.y + 140);
        g.drawString(strM2, papanGame.x + papanGame.width / 2 - (strM2.length() * 5), papanGame.y + 180);
    }
        
    }
    
}
