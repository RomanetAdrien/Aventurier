package agent;

import environnement.City;

/**
 * Created by adrie on 05/04/2018.
 */
public class Agent {

    protected int hp;
    protected int energy;
    protected String strategy;
    protected City currentCity;
    protected City nextCity;


    public void chooseNextCity(){
        switch (strategy){
            case "Brutal":chooseNextCityBrutal();break;
            default:chooseNextCityBrutal();break;
        }
    }

    public void chooseNextCityBrutal(){
        City next = currentCity;
        for(City city : currentCity.getNeighbors()){
            if(city.getDistanceToGoal()<=next.getDistanceToGoal()){
                next = city;
            }
        }
        nextCity=next;
    }



}
