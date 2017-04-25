package com.cds.learn.chapter3.methodreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chendongsheng5 on 2017/4/25.
 *
 */
class Zoo {
  private List animalList;
  public Zoo(List animalList) {
    this.animalList = animalList;
    System.out.println("Zoo created.");
  }
}

interface ZooFactory {
  Zoo getZoo(List animals);
}

public class ConstructorReferenceExample {

  public static void main(String[] args) {
    //用这个来说明method reference
    ZooFactory zooFactory = new ZooFactory() {
      @Override
      public Zoo getZoo(List animals) {
        return new Zoo(animals);
      }
    };
    //ZooFactory zooFactory = (List animalList)-> {return new Zoo(animalList);};
//    ZooFactory zooFactory = Zoo::new;
    System.out.println("Ok");
    Zoo zoo = zooFactory.getZoo(new ArrayList());
  }
}
