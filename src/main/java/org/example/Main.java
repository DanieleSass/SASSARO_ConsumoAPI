import org.example.API;
import org.example.Pokemon;

import static java.lang.IO.println;
import static java.lang.IO.readln;

void main(){

    API api = API.getInstance();
    try{
        println(api.getPokemonByName(readln("Inserisci pokemon: ")));
    }catch (Exception e){
        System.err.println(e.getMessage());
    }

}