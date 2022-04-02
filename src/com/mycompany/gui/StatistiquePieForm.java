/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gui;


import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.views.PieChart;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextArea;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import com.mycompany.entities.Publication;
import com.mycompany.services.ServicePublication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Chédy
 */

public class StatistiquePieForm extends BaseForm {
      Form current;
BaseForm form;
        
    private boolean drawOnMutableImage;
    
    
   
    

        public StatistiquePieForm(Resources res)  {
        super("Newsfeed", BoxLayout.y());
            current= this;

        Toolbar tb = new Toolbar(true);
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Acceuill");
        getContentPane().setScrollVisible(false);
        
        super.addSideMenu(res);
        tb.addSearchCommand(e -> {});
        
        Tabs swipe = new Tabs();

        Label spacer1 = new Label();
        Label spacer2 = new Label();
        addTab(swipe, res.getImage("logo.png"), spacer1, "Bienvenue");
                
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
        refreshTheme();
        Component.setSameSize(radioContainer, spacer1, spacer2);
        add(LayeredLayout.encloseIn(swipe, radioContainer));
        
         ButtonGroup barGroup = new ButtonGroup();
        RadioButton Publications = RadioButton.createToggle("Publications", barGroup);
        Publications.setUIID("SelectBar");
        RadioButton pub = RadioButton.createToggle("Statistiques", barGroup);
        pub.setUIID("SelectBar");
        RadioButton ajout = RadioButton.createToggle("Ajouter", barGroup);
       ajout.setUIID("SelectBar");

       
           // click   3 boutons  ajout stat publications 
        ajout.addActionListener((e) -> {
      
        new AjoutPublicationForm(res).show();
     
        
        
    });
        
         pub.addActionListener((e) -> {
   new StatistiquePieForm(res).show();
        
    });
         
            Publications.addActionListener((e) -> {
   new ListPublicationForm(res).show();
        
    });
        
        add(LayeredLayout.encloseIn(
                GridLayout.encloseIn(3,Publications, pub, ajout)
                
        ));
        
        ajout.setSelected(true);

        
      createPieChartForm();
        
        }
    
    
    
    
    
    

    
    private void addTab(Tabs swipe, Image img, Label spacer, String text) {
        int size = Math.min(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());
        if(img.getHeight() < size) {
            img = img.scaledHeight(size);
        }

        if(img.getHeight() > Display.getInstance().getDisplayHeight() / 2) {
            img = img.scaledHeight(Display.getInstance().getDisplayHeight() / 2);
        }
        ScaleImageLabel image = new ScaleImageLabel(img);
        image.setUIID("Container");
        image.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        Label overlay = new Label(" ", "ImageOverlay");
        
        Container page1 = 
            LayeredLayout.encloseIn(
                image,
                overlay,
                BorderLayout.south(
                    BoxLayout.encloseY(
                            new SpanLabel(text, "LargeWhiteText"),
                            spacer
                        )
                )
            );

        swipe.addTab("", page1);
    }
    
   private void addButton(Image img,String title) {
          int height = Display.getInstance().convertToPixels(11.5f);
        int width = Display.getInstance().convertToPixels(14f);
        Button image = new Button(img.fill(width, height));
        image.setUIID("Label");
        Container cnt = BorderLayout.west(image);
        cnt.setLeadComponent(image);
        TextArea ta = new TextArea(title);
        ta.setUIID("NewsTopLine");
        ta.setEditable(false);

  ;       
      
       cnt.add(BorderLayout.CENTER, 
               BoxLayout.encloseY(
                       ta
               ));
       
       image.addActionListener(e -> {
           try{
           new AjoutPublicationForm(Resources.getGlobalResources()).show();
           }catch(Exception exx) {
               
           }
               });
        add(cnt);
        image.addActionListener(e -> ToastBar.showMessage(title, FontImage.MATERIAL_INFO));
   }
   
    
// stat size label + margin
    public DefaultRenderer buildCatRendrer( int [] colors ){
    
    DefaultRenderer renderer = new DefaultRenderer();
    
    renderer.setLabelsTextSize(15);
    renderer.setLegendTextSize(15);
    renderer.setMargins(new int [] {20,30,15,0});
    for(int color : colors) {
    
    SimpleSeriesRenderer SimpleSeriesRenderer = new SimpleSeriesRenderer();
    
    SimpleSeriesRenderer.setColor(color);
    
    renderer.addSeriesRenderer(SimpleSeriesRenderer);
    
    }
    return renderer;
    }
    
    
    
    public void createPieChartForm (){
        
         int partage = 0;
    int culture = 0;
  
    ArrayList<Publication> p = ServicePublication.getInstance().affichagePublications();
    for (int i=0;i<p.size();i++)
    {
        switch (p.get(i).getNom().toLowerCase()) {
            case "partage":
                partage++;
                break;
            case "culture":
                culture++;
                break;
            default:
              
                break;
        }
    }
     HashMap<String, Integer> values = new HashMap<>();
    values.put("partage", partage);
    values.put("culture", culture);
   
    double total = partage+culture;
    
    double ppartage = (partage*100)/total;
    double pculture = (culture*100)/total;
    
    // colors set
    
    int [] colors = new int [] {0xf4b342,0x52b29a};
    DefaultRenderer renderer =buildCatRendrer(colors);
    renderer.setLabelsColor(0x000000); // black for label 
    renderer.setZoomButtonsVisible(true);
    renderer.setLabelsTextSize(40);
    renderer.setZoomEnabled(true);
    renderer.setChartTitleTextSize(20);
    renderer.setDisplayValues(true);
    renderer.setShowLabels(true);
    SimpleSeriesRenderer r=renderer.getSeriesRendererAt(0);
    r.setHighlighted(true);
    
    
    // cretae chart
    PieChart chart = new PieChart(buildDataset("title",Math.round(ppartage),Math.round(pculture)),renderer);
    
    ChartComponent c = new ChartComponent(chart);
    String [] messages = { "statistique publications"};
    SpanLabel message = new SpanLabel( messages[0],"welcome");
    Container cnt =BorderLayout.center(message);
    cnt.setUIID("container");
    add(cnt);
    add(c);
    
    
    
    
    }

    private CategorySeries buildDataset(String title, double ppartage,double pculture) {
       
        
        
        CategorySeries series = new CategorySeries(title);
        series.add("partage",ppartage);
               series.add("culture",pculture);
        return series;
    }
    
}
    