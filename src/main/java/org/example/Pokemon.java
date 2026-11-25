package org.example;

import java.util.List;

public record Pokemon(int id, String name, List<Types> types) {

    @Override
    public String toString(){
        String daReturnare="ID: "+ id+"\nNome: "+name
                +"\nTipo1: "+types.get(0).type().name();

        if(types.size()>1){
            daReturnare+="\nTipo2: "+types.get(1).type().name();
        }

        return daReturnare;
    }
}
