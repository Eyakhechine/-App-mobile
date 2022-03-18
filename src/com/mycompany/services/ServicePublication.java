/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.services;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import com.mycompany.entities.Publication;
import com.mycompany.utils.Statics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author EYA
 */
public class ServicePublication {
    
    public static ServicePublication instance =null;
    public static boolean resultOk = true;
    
    private ConnectionRequest req;
    
    public static ServicePublication getInstance(){
    
    if ( instance == null )
    instance = new ServicePublication();
    return instance;}
    
    
    public ServicePublication(){
    
    req = new ConnectionRequest();
    
    }
    // ajout 
    public void ajoutPublication (Publication publication){
    
    String url=Statics.BASE_URL+"/addPublication?nom="+publication.getNom()+"&description="+publication.getDescription();
    req.setUrl(url);
    req.addResponseListener((e) -> {
    
    String str = new String(req.getResponseData());
            System.out.println("data=="+str);
    
    });
    NetworkManager.getInstance().addToQueueAndWait(req);
    }
    
    // affichage 
    
    public ArrayList<Publication>affichagePublications(){
    ArrayList<Publication> result = new ArrayList<>();
    String url=Statics.BASE_URL+"/displaypublications";
    req.setUrl(url);
    
   req.addResponseListener(new ActionListener<NetworkEvent>(){
   
   @Override
   public void actionPerformed(NetworkEvent evt ){
   
   JSONParser jsonp;
   jsonp = new JSONParser();
   
   try {
   
   Map<String,Object>mapPublications=jsonp.parseJSON(new CharArrayReader(new String (req.getResponseData()).toCharArray()));
   List<Map<String,Object>> listOfMaps =(List<Map<String,Object>>) mapPublications.get("root");
   for (Map<String,Object> obj : listOfMaps){
   
   Publication p = new Publication();
   float id =Float.parseFloat(obj.get("id").toString());
   String nom =obj.get("nom").toString();
   String description =obj.get("description").toString();
   
   p.setId((int)id);
   p.setNom(nom);
    p.setDescription(description);
   
   result.add(p);
   
   }
   }catch (Exception ex){
   ex.printStackTrace();
   
   }
   
   }
   
   
   });
   
    
      NetworkManager.getInstance().addToQueueAndWait(req);
      return result;
    
    
  
    
    
    }
     
    
    
    
    
        public boolean deletePublication(int id ){

 String url=Statics.BASE_URL+"/deletePublication?id="+id;
    req.setUrl(url);
    
    
       req.addResponseListener(new ActionListener<NetworkEvent>(){
   
   @Override
   public void actionPerformed(NetworkEvent evt ){
   
   req.removeResponseCodeListener(this);
   }
   });
 NetworkManager.getInstance().addToQueueAndWait(req);
      return resultOk;
    
    
    
    }
    

        
        
        
        public boolean modifierPublication(Publication publication){
        
            String url=Statics.BASE_URL+"/updatePublication?id="+publication.getId()+"&nom="+publication.getNom()+"&description="+publication.getDescription();
            req.setUrl(url);
    
    
       req.addResponseListener(new ActionListener<NetworkEvent>(){
   
   @Override
   public void actionPerformed(NetworkEvent evt ){
   
   resultOk = req.getResponseCode()== 200; // response http
   req.removeResponseListener(this);
   }
   });
       
        NetworkManager.getInstance().addToQueueAndWait(req); // execution request 
      return resultOk;
        
        }
        
        
        
        
        
        
        
        
        
        
        
        
        
        
}
    

