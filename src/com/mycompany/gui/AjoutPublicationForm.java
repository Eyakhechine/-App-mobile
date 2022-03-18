/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gui;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.BOTTOM;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import com.mycompany.entities.Publication;
import com.mycompany.services.ServicePublication;

/**
 *
 * @author EYA
 */
public class AjoutPublicationForm extends BaseForm {
    Form current;
    public AjoutPublicationForm(Resources res ){
    
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
        RadioButton pub = RadioButton.createToggle("autres", barGroup);
        pub.setUIID("SelectBar");
        RadioButton ajout = RadioButton.createToggle("Ajouter publication", barGroup);
       ajout.setUIID("SelectBar");
    
        Label arrow = new Label(res.getImage("news-tab-down-arrow.png"), "Container");
        
        Publications.addActionListener((e) -> {
        InfiniteProgress ip = new InfiniteProgress();
        final Dialog ipD1g= ip.showInfiniteBlocking();
        
        refreshTheme();
        
    });
        
        
        
        
        add(LayeredLayout.encloseIn(
                GridLayout.encloseIn(3,Publications, pub, ajout),
                FlowLayout.encloseBottom(arrow)
        ));
        
        ajout.setSelected(true);
        arrow.setVisible(false);
        addShowListener(e -> {
            arrow.setVisible(true);
            updateArrowPosition(ajout, arrow);
        });
        bindButtonSelection(Publications, arrow);
        bindButtonSelection(pub, arrow);
        bindButtonSelection(ajout, arrow);
       
        
        // special case for rotation
        addOrientationListener(e -> {
            updateArrowPosition(barGroup.getRadioButton(barGroup.getSelectedIndex()), arrow);
        });
    
    
    
    
    //
    
    
    TextField nom = new TextField("","saisir le nom  !");
    nom.setUIID("TextFieldBlack");
    addStringValue("nom",nom);
    
     TextField description = new TextField("","saisir la description  !");
    nom.setUIID("TextFieldBlack");
    addStringValue("description",description);
    
    
    Button btnAjouter = new Button("Ajouter");
    addStringValue("",btnAjouter);
    
    
    btnAjouter.addActionListener((e) -> {
    
    try {
    
        if (nom.getText().equals("") || description.getText().equals(""))
        {
        Dialog.show("vérifier vos données","","Annuler","ok");
        
        }
        else {
            InfiniteProgress ip = new InfiniteProgress();;
            final Dialog iDialog = ip.showInfiniteBlocking();
            
            
            Publication p = new Publication(String.valueOf(nom.getText()
            ).toString(),
            String.valueOf(description.getText()).toString()
           );
            
            System.out.println("publication =="+p);
            
            ServicePublication.getInstance().ajoutPublication(p);
            iDialog.dispose();
            
            // Affichage 
            
            new ListPublicationForm(res).show();
            
            refreshTheme();
        }
            
            
            
            
    
    
    }catch( Exception ex) {
    
    ex.printStackTrace();
    }
    
    
    });
    
    
    
    
    }

    private void addStringValue(String s, Component v) {
       add(BorderLayout.west(new Label(s,"PaddedLabel"))
       
       .add(BorderLayout.CENTER,v));
       add(createLineSeparator(0xeeeeee));
       
       
       
    }

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
    
    public void bindButtonSelection(Button btn,Label l){
    
    
    btn.addActionListener(e ->  {
    
        if(btn.isSelected()) {
        
        updateArrowPosition(btn,l);
        
        }
    
    
    });
    }

    private void updateArrowPosition(Button btn, Label l) {
        l.getUnselectedStyle().setMargin(LEFT,btn.getX() + btn.getWidth() / 2 - l.getWidth() / 2  );
        l.getParent().repaint();
        
        
        
    }
    
    
}
