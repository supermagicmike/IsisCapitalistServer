/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.Capitalist;

import generated.PallierType;
import generated.ProductType;
import generated.ProductsType;
import generated.World;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Mick
 */
public class Services {

    World readWorldFromXml(String username) throws JAXBException {
        JAXBContext cont = JAXBContext.newInstance(World.class);
        Unmarshaller u = cont.createUnmarshaller();
        World world;

        if (new File(username + "_world.xml").exists()) {
            System.out.println("find !");
            world = (World) u.unmarshal(new File(username + "_world.xml"));
        } else {
            InputStream input = getClass().getClassLoader().getResourceAsStream("world.xml");
            System.out.println("world : ??" + input);
            world = (World) u.unmarshal(input);
        }
        updateScore(world);
        saveWorldToXml(world, username);
        return world;
    }

    void saveWorldToXml(World world, String username) {
        try {
            OutputStream output = new FileOutputStream(username + "_world.xml");
            JAXBContext cont = JAXBContext.newInstance(World.class);
            Marshaller m = cont.createMarshaller();
            m.marshal(world, output);

        } catch (Exception e) {

        }
    }

    public Boolean updateScore(World world) {
        ProductsType products = world.getProducts();
        List<ProductType> listProducts = products.getProduct();
        for (ProductType p : listProducts){
            if (p.isManagerUnlocked()){
                long elapsed = System.currentTimeMillis()-world.getLastupdate();
                long money=(int)elapsed/p.getVitesse();
                world.setMoney(world.getMoney()+(p.getRevenu()*money));
                System.out.println(world.getMoney());
            }else{
                if(p.getQuantite()>0){
                if(p.getTimeleft()!= 0&&p.getTimeleft()<System.currentTimeMillis()-world.getLastupdate()){;
                     
                    System.out.println("money"+ world.getMoney()+"reven"+p.getRevenu());
                    world.setMoney(world.getMoney()+p.getRevenu());
                }else
                    p.setTimeleft(0);
               
            }}
        }
        world.setLastupdate(System.currentTimeMillis());
        return false;
    }

    public Boolean updateManager(String username, PallierType newmanager) throws JAXBException {
        World world = readWorldFromXml(username); // aller chercher le monde qui correspond au joueur 
        PallierType manager = findManagerByName(world, newmanager.getName()); // trouver dans ce monde, le manager équivalent à celui passé en paramètre 
        System.out.println("manager: " + newmanager.getName() + "managerprice::::: :::" + newmanager.getSeuil());
        if (manager == null) {
            return false;
        }
        manager.setUnlocked(true);// débloquer ce manager 
        ProductType product = findProductById(world, manager.getIdcible());  // trouver le produit correspondant au manager 
        if (product == null) {
            return false;
        }
        product.setManagerUnlocked(true);// débloquer le manager de ce produit 
        world.setMoney(world.getMoney() - manager.getSeuil()); // soustraire de l'argent du joueur le cout du manager 
        saveWorldToXml(world, username); // sauvegarder les changements au monde 
        updateScore (world);
        return true;
    }

    public Boolean updateProduct(String username, ProductType newproduct) throws JAXBException {
        World world = readWorldFromXml(username); // aller chercher le monde qui correspond au joueur 
        ProductType product = findProductById(world, newproduct.getId());         // trouver dans ce monde, le produit équivalent à celui passé en paramètre 
        if (product == null) {
            return false;
        }
        // calculer la variation de quantité. Si elle est positive c'est 
        // que le joueur a acheté une certaine quantité de ce produit 
        int qtchange = newproduct.getQuantite() - product.getQuantite();
        if (qtchange > 0) {
            world.setMoney(world.getMoney() - (getRealPrice(product) * ((1 - Math.pow(product.getCroissance(), qtchange)) / (1 - product.getCroissance())))); // soustraire de l'argent du joueur le cout de la quantité achete
            product.setQuantite(product.getQuantite() + qtchange); // mettre à jour la quantité de product 
        } else { // sinon c’est qu’il s’agit d’un lancement de production. 
            product.setTimeleft(newproduct.getVitesse());// initialiser product.timeleft à product.vitesse pour lancer la production 
        }
        // sauvegarder les changements du monde 
        saveWorldToXml(world, username);
        updateScore(world);
        return true;
    }

    private ProductType findProductById(World world, int id) {
        ProductType res = null;
        for (ProductType p : world.getProducts().getProduct()) {
            if (p.getId() == id) {
                res = p;
            }
        }
        return res;
    }

    private Double getRealPrice(ProductType p) {
        Double d;
        d = p.getCout() * Math.pow(p.getCroissance(), p.getQuantite());
        return d;
    }

    private PallierType findManagerByName(World world, String name) {
        PallierType res = null;
        for (PallierType t : world.getManagers().getPallier()) {
            if (t.getName().equals(name)) {
                res = t;
            }
        }
        return res;
    }

}
