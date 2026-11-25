import org.example.API;
import org.example.Database;
import org.example.Pokemon;

import java.sql.SQLException;

import static java.lang.IO.println;
import static java.lang.IO.readln;

void main(){

    API api = API.getInstance();
    Pokemon pokemon;

    String fonteDato="";

    println("Inserire '0' per uscire");
    try{
        Database db = Database.getInstance();
        while(true){

            try{
                String input=readln("Inserisci nome o id del pokemon: ");
                if(input.equals("0")){
                    break;
                }

                //se è un numero allora la ricerca è per id
                if(isInteger(input))
                {
                    int id = Integer.parseInt(input);
                    pokemon = db.getPokemon(id);
                    if(pokemon==null){  //non è già nel db
                        //lo prende grazie ad APi
                        try{
                            pokemon=api.getPokemonById(id);
                            //e lo salva nel db x le prossime operazioni
                            db.insertPokemon(pokemon);
                            fonteDato="API";
                        }
                        catch (IOException e){
                            println("Pokemon non trovato!");
                            continue;
                        }

                    }
                    else{
                        fonteDato="DB";
                    }
                }
                else
                {
                    //allora lo cerca per nome
                    pokemon=db.getPokemon(input);
                    if(pokemon==null){  //non è già nel db
                        //lo prende grazie ad APi
                        try{
                            pokemon=api.getPokemonByName(input);
                            //e lo salva nel db x le prossime operazioni
                            db.insertPokemon(pokemon);
                            fonteDato="API";
                        }
                        catch (IOException e){
                            println("Pokemon non trovato!");
                            continue;
                        }

                    }
                    else{
                        fonteDato="DB";
                    }
                }

                println(pokemon);
                println("Dato preso da: " + fonteDato);
            }catch (Exception e){
                System.err.println(e.getMessage());
            }

        }
    }
    catch (SQLException e){
        System.err.println(e.getMessage());
    }

}

static boolean isInteger(String input){
    try{
        Integer.parseInt(input);
        return true;
    }
    catch (NumberFormatException e ){
        return false;
    }
}