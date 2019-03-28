/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.Capitalist;

import generated.World;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Mick
 */
public class Services {
    
    World readWorldFromXml() throws JAXBException{
        InputStream input =   getClass().getClassLoader().getResourceAsStream("world.xml");
            JAXBContext cont = JAXBContext.newInstance(World.class);
            Unmarshaller u = cont.createUnmarshaller();
            World world = (World) u.unmarshal(input);
        return world;
    }
    
    
    void saveWorldToXml(World world) throws JAXBException, FileNotFoundException{
        JAXBContext cont = JAXBContext.newInstance(World.class);
        Marshaller m = cont.createMarshaller();
        File file = new File("world.xml");
        m.marshal(world, file);
        OutputStream  output = new FileOutputStream(file); 
    }
}
