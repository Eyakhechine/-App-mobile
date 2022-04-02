/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gui;

import com.codename1.components.FloatingHint;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.mycompany.entities.Publication;
import com.mycompany.services.ServicePublication;

/**
 *
 * @author EYA
 */
public class ModifierPublicationForm extends BaseForm {
    
    Form current;
    public ModifierPublicationForm(Resources res , Publication p){
     super ("Newsfeed",BoxLayout.y());
    Toolbar tb = new Toolbar(true);
    current = this;
    setToolbar(tb);
    getTitleArea().setUIID("Container");
    setTitle("modifier publications");
    getContentPane().setScrollVisible(false);
    
    super.addSideMenu(res);
    
    TextField nom = new TextField(p.getNom(),"nom",20,TextField.ANY);
     TextField description = new TextField(p.getDescription(),"description",20,TextField.ANY);
     
     nom.setUIID("NewsTopLine");
     description.setUIID("NewsTopLine");
     
     nom.setSingleLineTextArea(true);
      description.setSingleLineTextArea(true);
      
      
      Button btnModifier = new Button("Modifier");
      btnModifier.setUIID("Button");
      
      btnModifier.addPointerPressedListener(l -> {
      
      p.setNom(nom.getText());
       p.setDescription(description.getText());
      
      
     
      
      // appel fn modif
      
      if (ServicePublication.getInstance().modifierPublication(p)){
      
      new ListPublicationForm(res).show();
      
      
      }
       });
      
       Button btnAnnuler = new Button("Annuler");
       btnAnnuler.addActionListener(e -> {
       
        new ListPublicationForm(res).show();
       
       
       });
       
       
       Label l2 = new Label("");
        Label l1 = new Label();
        
        Container content = BoxLayout.encloseY(
        l1,l2,
                
        new FloatingHint (nom),
        createLineSeparator(),
        new FloatingHint (description),
         createLineSeparator(),
         btnModifier,
         btnAnnuler
         
        
        );
        
       add(content);
       show();
      
      
    
    
    }
    
    
    
    
    
}
