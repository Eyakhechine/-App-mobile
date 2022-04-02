/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gui;

import com.codename1.components.AudioRecorderComponent;
import com.codename1.components.FloatingHint;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.File;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.location.Location;
import com.codename1.location.LocationManager;
import com.codename1.maps.Coord;
import com.codename1.maps.MapComponent;
import com.codename1.maps.layers.LinesLayer;
import com.codename1.maps.layers.PointLayer;
import com.codename1.maps.layers.PointsLayer;
import com.codename1.media.MediaManager;
import com.codename1.media.MediaRecorderBuilder;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.CN;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.BOTTOM;
import static com.codename1.ui.Component.CENTER;
import static com.codename1.ui.Component.LEFT;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Sheet;
import com.codename1.ui.Stroke;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextArea;
import com.codename1.ui.Toolbar;
import com.codename1.ui.Transform;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.GeneralPath;
import com.codename1.ui.geom.Shape;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.util.AsyncResource;
import com.mycompany.entities.Publication;
import com.mycompany.services.ServicePublication;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.Vector;


/**
 *
 * @author EYA
 */
public class ListPublicationForm extends BaseForm {
    Form current;
       
   
    public ListPublicationForm(Resources res) {
    
     super ("Newsfeed",BoxLayout.y());
    Toolbar tb = new Toolbar(true);
    current = this;
    setToolbar(tb);
    getTitleArea().setUIID("Container");
    setTitle("ajout publications");
    getContentPane().setScrollVisible(false);
    
    
    tb.addSearchCommand(e ->{
    
    });
    
    Tabs swipe = new Tabs();
    
    Label s1 = new Label();
    Label s2 = new Label();
    addTab(swipe,s1,res.getImage("logo.png"),"","",res);
    
    //
     
     swipe.setUIID("Container");
        swipe.getContentPane().setUIID("Container");
        swipe.hideTabs();
        
        ButtonGroup bg = new ButtonGroup();
        int size = Display.getInstance().convertToPixels(1);
        Image unselectedWalkthru = Image.createImage(size, size, 0);
        Graphics g = unselectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAlpha(100);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        Image selectedWalkthru = Image.createImage(size, size, 0);
        g = selectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        RadioButton[] rbs = new RadioButton[swipe.getTabCount()];
        FlowLayout flow = new FlowLayout(CENTER);
        flow.setValign(BOTTOM);
        Container radioContainer = new Container(flow);
        for(int iter = 0 ; iter < rbs.length ; iter++) {
            rbs[iter] = RadioButton.createToggle(unselectedWalkthru, bg);
            rbs[iter].setPressedIcon(selectedWalkthru);
            rbs[iter].setUIID("Label");
            radioContainer.add(rbs[iter]);
        }
                
        rbs[0].setSelected(true);
        swipe.addSelectionListener((i, ii) -> {
            if(!rbs[ii].isSelected()) {
                rbs[ii].setSelected(true);
            }
        });
        
        Component.setSameSize(radioContainer, s1, s2);
        add(LayeredLayout.encloseIn(swipe, radioContainer));
        
        ButtonGroup barGroup = new ButtonGroup();
        
        
        RadioButton Publications = RadioButton.createToggle("Publications", barGroup);
        Publications.setUIID("SelectBar");
        RadioButton pub = RadioButton.createToggle("Statistiques", barGroup);
        pub.setUIID("SelectBar");
        RadioButton ajout = RadioButton.createToggle("Ajouter ", barGroup);
       ajout.setUIID("SelectBar");
    
      
        // click   3 boutons  ajout stat publications 
        ajout.addActionListener((e) -> {
        InfiniteProgress ip = new InfiniteProgress();
        final Dialog ipD1g= ip.showInfiniteBlocking();
        new AjoutPublicationForm(res).show();
     
        refreshTheme();
        
    });
        
         pub.addActionListener((e) -> {
   new StatistiquePieForm(res).show();
        
    });
         
            Publications.addActionListener((e) -> {
   new ListPublicationForm(res).show();
        
    });
        // record voice code
       
      
      
        Button record = new Button("Record Audio");
        record.addActionListener(e->{
            recordAudio().onResult((resu, err)->{
                if (err != null) {
                    Log.e(err);
                    ToastBar.showErrorMessage(err.getMessage());
                    return;
                }
                if (resu == null) {
                    return;
                }
                try {
                    MediaManager.createMedia(resu, false).play();
                } catch (IOException ex) {
                    Log.e(ex);
                    ToastBar.showErrorMessage(ex.getMessage());
                }
            });
        });
        this.add(record);
        this.show();
        
        
    // end record voice  code

        add(LayeredLayout.encloseIn(
                GridLayout.encloseIn(3,Publications, pub, ajout)
         
        ));
        
        Publications.setSelected(true);

    // appel affichage 
    ArrayList<Publication> list = ServicePublication.getInstance().affichagePublications();
    
    for(Publication p : list ) {
        
        
    String urlImage= ("logo.png");
    Image PlaceHolder= Image.createImage(120,90);
    EncodedImage enc=EncodedImage.createFromImage(PlaceHolder, false);
    URLImage urlim = URLImage.createToStorage(enc, urlImage, urlImage,URLImage.RESIZE_SCALE);
            
            
    addButton(urlim,p,res);
    
    ScaleImageLabel image = new ScaleImageLabel(urlim);
    Container containerImg = new Container();
    image.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
    
    
    }
    
     

      // BOUTON CONSULTER L HEURE 
       Button btnCLOCK = new Button("consulter l'heure");
       btnCLOCK.addActionListener(e -> {
       
        Form cl = new Form("Clock Demo");
        AnalogClock clock = new AnalogClock();
        cl.setLayout(new BorderLayout());
        cl.addComponent(BorderLayout.CENTER, clock);
        clock.start();
        cl.show();

       });
       // bouton map 
          Button btnmap = new Button("consulter map");
       btnmap.addActionListener(e -> {
       
       new MapForm();

       });
    
       Label l2 = new Label("");
        Label l1 = new Label();   
        Container content = BoxLayout.encloseY(
        l1,l2,
         createLineSeparator(),
         btnCLOCK ,
                 createLineSeparator(),
         btnmap
        );
        
       add(content);
       show();
 
    }// fermeture constructeur
    
    
     private void addTab(Tabs swipe,Label spacer, Image image, String string, String text, Resources res) {
        int size =Math.min(Display.getInstance().getDisplayWidth(),Display.getInstance().getDisplayHeight());
        
        if (image.getHeight()< size ) {
        
        image=image.scaledHeight(size);
        
        }
        
        
        if( image.getHeight()> Display.getInstance().getDisplayHeight() / 2 ) {
        
        image =image.scaledHeight(Display.getInstance().getDisplayHeight() / 2 );
        }
        ScaleImageLabel imageScale = new ScaleImageLabel(image);
        imageScale.setUIID("Container");
        imageScale.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        
        Label overLay = new Label ("","ImageOverLay");
        
        Container page1 =
                LayeredLayout.encloseIn(
                        imageScale,
                        overLay,
                        BorderLayout.south(
                        BoxLayout.encloseY(
                         new SpanLabel(text,"LargeWhiteText"),
                    
                        
                                spacer
                                )
                        )
                );
                        
                  swipe.addTab("",res.getImage("logo.png"),page1 )    ;  
    }


    private void addButton(Image img,Publication p,Resources res) {
        int height =Display.getInstance().convertToPixels(11.5f);
        
        int width =Display.getInstance().convertToPixels(14f);
        
        Button image = new Button(img.fill(width,height));
        image.setUIID("Label");
        Container cnt = BorderLayout.west(image);
        
     Label nomText = new Label ("nom:"+p.getNom(),"NewsTopLine2");
      Label descriptionText = new Label ("Description:"+p.getDescription(),"NewsTopLine2");
      
 
        
       
        
             // supprimer button 
             
             Label lSupprimer= new Label(" ");
             lSupprimer.setUIID("NewsTopLine");
             Style supprimerStyle = new Style(lSupprimer.getUnselectedStyle());
             supprimerStyle.setFgColor(0xf21f1f);
             
             FontImage supprimerImage =FontImage.createMaterial(FontImage.MATERIAL_DELETE, supprimerStyle);
             lSupprimer.setIcon(supprimerImage);
             lSupprimer.setTextPosition(RIGHT);
             
             
             // appel service
             lSupprimer.addPointerPressedListener(l -> {
             
             Dialog dig = new Dialog("suppression ");
             if (dig.show("suppression","vouslez-vous supprimer cette publication?","Annuler","oui")){
             
             dig.dispose();
             }
             else {
                       dig.dispose();
                     
                     if(ServicePublication.getInstance().deletePublication(p.getId()))  {
                     new ListPublicationForm(res).show();
                     
                     
                     
                     }
                     }
             
             
             
             
             });
             
             // update 
             
             
                Label lModifier= new Label(" ");
           lModifier.setUIID("NewsTopLine");
             Style modifierStyle = new Style(lModifier.getUnselectedStyle());
             modifierStyle.setFgColor(0xf7ad02);
             
             FontImage mFontImage=FontImage.createMaterial(FontImage.MATERIAL_MODE_EDIT, modifierStyle);
                 lModifier.setIcon(mFontImage);
             lModifier.setTextPosition(LEFT);
             
                   // appel service modifier 
             lModifier.addPointerPressedListener(l -> {
             
             //System.out.println("modifier");
             new ModifierPublicationForm(res,p).show();
             
             
             
             });
             
             
             
              cnt.add(BorderLayout.CENTER,BoxLayout.encloseY
        (BoxLayout.encloseX(nomText,lModifier,lSupprimer),
                BoxLayout.encloseX(descriptionText)));
                     
        add(cnt);
    }
   // METHODES clock
    
    public class AnalogClock extends Component {

    Date currentTime = new Date();
    long lastRenderedTime = 0;
    

    @Override
    public boolean animate() {
        if ( System.currentTimeMillis()/1000 != lastRenderedTime/1000){
            currentTime.setTime(System.currentTimeMillis());
            return true;
        }
        return false;
    }
    
    public void start(){
        this.getComponentForm().registerAnimated(this);
    }
    
    public void stop(){
        this.getComponentForm().deregisterAnimated(this);
    }
    
    @Override
    public void paintBackground(Graphics g) {
        super.paintBackground(g);
        
        boolean oldAntialiased = g.isAntiAliased();
        g.setAntiAliased(true);
        //g.setColor(0x000000);
        //g.drawString("Clock", getX(), getY());
        double padding = 10;
        double r = Math.min(getWidth(), getHeight())/2-padding;
        double cX = getX()+getWidth()/2;
        double cY = getY()+getHeight()/2;
        
        // draw the ticks
        int tickLen = 10;
        int medTickLen = 30;
        int longTickLen = 50;
        int tickColor = 0xCCCCCC;
        Stroke tickStroke = new Stroke(2f, Stroke.CAP_BUTT, Stroke.JOIN_ROUND, 1f);
        
        GeneralPath ticksPath = new GeneralPath();
        
        for ( int i=1; i<= 60; i++){
            int len = tickLen;
            if ( i % 15 == 0 ){
                len = longTickLen;
            } else if ( i % 5 == 0 ){
                len = medTickLen;
            }
            double di = (double)i;
            double angleFrom12 = di/60.0*2.0*Math.PI;
            double angleFrom3 = Math.PI/2.0-angleFrom12;
            ticksPath.moveTo(
                    (float)(cX+Math.cos(angleFrom3)*r),
                    (float)(cY-Math.sin(angleFrom3)*r)
            );
            
            ticksPath.lineTo(
                    (float)(cX+Math.cos(angleFrom3)*(r-len)),
                    (float)(cY-Math.sin(angleFrom3)*(r-len))
            );
        }
        g.setColor(tickColor);
        g.drawShape(ticksPath, tickStroke);
        
        g.setColor(0x000000);
        // Draw the numbers
        
        
        for ( int i=1; i<=12; i++){
            String numStr = ""+i;
            int charWidth = g.getFont().stringWidth(numStr);
            int charHeight = g.getFont().getHeight();
            double di = (double)i;
            double angleFrom12 = di/12.0*2.0*Math.PI;
            double angleFrom3 = Math.PI/2.0-angleFrom12;
            //g.rotate((float)angleFrom12, (int)absCX, (int)absCY);
            int tx = (int)(Math.cos(angleFrom3)*(r-longTickLen));
            int ty = (int)(-Math.sin(angleFrom3)*(r-longTickLen));
            
            if ( i == 6 ){
                ty -= charHeight/2;
            } else if ( i == 12 ){
                ty += charHeight/2;
            }
            
            g.translate(
                    tx,
                    ty
            );
            
            
            g.drawString(numStr, (int)cX-charWidth/2, (int)cY-charHeight/2);
            g.translate(-tx, -ty);
            
            
        }
        
        
        // Draw the hands of the clock
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        
        GeneralPath secondHand = new GeneralPath();
        secondHand.moveTo((float)cX, (float)cY);
        secondHand.lineTo((float)cX, (float)(cY-(r-medTickLen)));
        
        
        Shape translatedSecondHand = secondHand.createTransformedShape(Transform.makeTranslation(0f, 5, 0));
        
        double second = (double)(calendar.get(Calendar.SECOND));
        
        double secondAngle = second/60.0*2.0*Math.PI;
        
        double absCX = getAbsoluteX()+cX-getX();
        double absCY = getAbsoluteY()+cY-getY();
        
        g.rotate((float)secondAngle, (int)absCX, (int)absCY);
        g.setColor(0xff0000);
        g.drawShape(translatedSecondHand, new Stroke(2f, Stroke.CAP_BUTT, Stroke.JOIN_BEVEL, 1f));
        g.resetAffine();
        
        GeneralPath minuteHand = new GeneralPath();
        minuteHand.moveTo((float)cX, (float)cY);
        minuteHand.lineTo((float)cX+6, (float)cY);
        minuteHand.lineTo((float)cX+2, (float)(cY-(r-tickLen)));
        minuteHand.lineTo((float)cX-2, (float)(cY-(r-tickLen)));
        minuteHand.lineTo((float)cX-6, (float)cY);
        minuteHand.closePath();
        
        Shape translatedMinuteHand = minuteHand.createTransformedShape(Transform.makeTranslation(0f, 5, 0));
        
        double minute = (double)(calendar.get(Calendar.MINUTE)) + 
                (double)(calendar.get(Calendar.SECOND))/60.0;
        
        double minuteAngle = minute/60.0*2.0*Math.PI;
        g.rotate((float)minuteAngle, (int)absCX, (int)absCY);
        g.setColor(0x000000);
        g.fillShape(translatedMinuteHand);
        g.resetAffine();
        
        GeneralPath hourHand = new GeneralPath();
        hourHand.moveTo((float)cX, (float)cY);
        hourHand.lineTo((float)cX+4, (float)cY);
        hourHand.lineTo((float)cX+1, (float)(cY-(r-longTickLen)*0.75));
        hourHand.lineTo((float)cX-1, (float)(cY-(r-longTickLen)*0.75));
        hourHand.lineTo((float)cX-4, (float)cY);
        hourHand.closePath();
        
        Shape translatedHourHand = hourHand.createTransformedShape(Transform.makeTranslation(0f, 5, 0));
        
        //Calendar cal = Calendar.getInstance().get
        double hour = (double)(calendar.get(Calendar.HOUR_OF_DAY)%12) + 
                (double)(calendar.get(Calendar.MINUTE))/60.0;
        
        double angle = hour/12.0*2.0*Math.PI;
        g.rotate((float)angle, (int)absCX, (int)absCY);
        g.setColor(0x000000);
        g.fillShape(translatedHourHand);
        g.resetAffine();
        
        
        g.setAntiAliased(oldAntialiased);
        lastRenderedTime=System.currentTimeMillis();
        
        
    }
    
    
}  
    
      //METHODES voice recorder
    
    private AsyncResource<String> recordAudio() {
        AsyncResource<String> out = new AsyncResource<>();
        String mime = MediaManager.getAvailableRecordingMimeTypes()[0];
        String ext = mime.indexOf("mp3") != -1? "mp3" : mime.indexOf("wav") != -1 ? "wav" : mime.indexOf("aiff") != -1 ? "aiff" : "aac";
        MediaRecorderBuilder builder = new MediaRecorderBuilder()
                .path(new File("myaudio."+ext).getAbsolutePath())
                .mimeType(mime);
        
        final AudioRecorderComponent cmp = new AudioRecorderComponent(builder);
        final Sheet sheet = new Sheet(null, "Record Audio");
        sheet.getContentPane().setLayout(new com.codename1.ui.layouts.BorderLayout());
        sheet.getContentPane().add(com.codename1.ui.layouts.BorderLayout.CENTER, cmp);
        cmp.addActionListener(new com.codename1.ui.events.ActionListener() {
            @Override
            public void actionPerformed(com.codename1.ui.events.ActionEvent e) {
                switch (cmp.getState()) {
                    case Accepted:
                        CN.getCurrentForm().getAnimationManager().flushAnimation(new Runnable() {
                            public void run() {
                                sheet.back();
                                sheet.addCloseListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent evt) {
                                        sheet.removeCloseListener(this);
                                        out.complete(builder.getPath());
                                    }

                                });
                            }
                        });
                        
                       
                        
                        break;
                    case Canceled:
                        FileSystemStorage fs = FileSystemStorage.getInstance();
                        if (fs.exists(builder.getPath())) {
                            FileSystemStorage.getInstance().delete(builder.getPath());
                        }
                        CN.getCurrentForm().getAnimationManager().flushAnimation(new Runnable() {
                            public void run() {
                                sheet.back();
                                sheet.addCloseListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent evt) {
                                        sheet.removeCloseListener(this);
                                        out.complete(null);
                                    }

                                });
                            }
                        });
                        
                       
                        break;
                }
            }
            
        });
        sheet.addCloseListener(new com.codename1.ui.events.ActionListener() {
            @Override
            public void actionPerformed(com.codename1.ui.events.ActionEvent e) {
                if (cmp.getState() != AudioRecorderComponent.RecorderState.Accepted && cmp.getState() != AudioRecorderComponent.RecorderState.Canceled) {
                    FileSystemStorage fs = FileSystemStorage.getInstance();
                    if (fs.exists(builder.getPath())) {
                        FileSystemStorage.getInstance().delete(builder.getPath());
                    }
                    CN.getCurrentForm().getAnimationManager().flushAnimation(new Runnable() {
                        public void run() {
                            out.complete(null);
                        }
                    });
                }
            }
            
        });
        sheet.show();
        return out;
    }
    
    
    
    
    }
    
