package de.framey.lab.evil.squishytentaclefun.travellinghenchman;

import java.util.ArrayList;
import java.util.List;

import de.framey.lab.evil.eviltentaclesofdeath.Tentacle;

/**
 * Calculate how our loyal henchmen need to travel to visit a bunch of locations without visiting any of them twice.
 * Because this solution is evil, it will use a nonderteministic approach.
 * 
 * @author Frank Meyfarth
 */
public class TravellingHenchman implements Tentacle {
    
    /** A little henchman of himself this will help us to solve nondeterminism. */
    private NondeterministicProblemHelper<String> any;
    
    /** Here we collect the routes our henchman may travel. */
    private List<List<String>>  validRoutes;
    
    /** Just a place to memorise how many places to visit. */
    private int locationCount;         
    
    /**
     * Main method to start it all off.
     * 
     * @param args
     *            No args needed
     */
    public static void main(String[] args) {
        new TravellingHenchman().calculateRoutes();
    }
    
    /**
     * Initialize internals.
     */
    public TravellingHenchman() {
        any         = new NondeterministicProblemHelper<String>("FINISHED", this::isRouteComplete, this::isNextLocationValid);
        validRoutes = new ArrayList<>();
    }

    /**
     * Calculate the valid routes for our henchmen by spreading
     * the tentacles of our evil empire all over the world.
     * 
     * Muhahahaha!!!
     */
    private void calculateRoutes() {
        locationCount = 9;
        
        GOTO(any.oneOf("Lissabon", "London", "Paris", "Rom", "Berlin", "Wien", "Stockholm", "Warschau", "Athen"));
        
        LABEL("Lissabon" ); GOTO(any.oneOf("London", "Rom"));
        LABEL("London"   ); GOTO(any.oneOf("Lissabon", "Paris"));
        LABEL("Paris"    ); GOTO(any.oneOf("London", "Berlin"));
        LABEL("Rom"      ); GOTO(any.oneOf("Lissabon", "Berlin", "Wien", "Athen"));
        LABEL("Berlin"   ); GOTO(any.oneOf("Stockholm", "Warschau", "Wien", "Rom", "Paris"));
        LABEL("Wien"     ); GOTO(any.oneOf("Berlin", "Rom"));
        LABEL("Stockholm"); GOTO(any.oneOf("Berlin", "Warschau"));
        LABEL("Warschau" ); GOTO(any.oneOf("Stockholm", "Berlin", "Athen"));
        LABEL("Athen"    ); GOTO(any.oneOf("Rom", "Warschau"));

        LABEL("FINISHED"); printValidRoutes();
    }
    
    /**
     * This tells our henchmen, if he may travel to the next location or not.
     * @param route the route he traveled so far
     * @param location the place he wants to go next
     * @return TRUE, if we allow him to
     */
    private boolean isNextLocationValid(List<String> route, String location) {
        return !route.contains(location);
    }

    /**
     * This tells our henchmen, if he's done with travelling 
     * and can proceed on other sinister tasks.
     * @param route the route he traveled so far
     * @return TRUE, if he is done
     */
    private boolean isRouteComplete(List<String> route) {
        boolean isComplete = route.size() == locationCount;
        if (isComplete) {
            validRoutes.add(route);
        }
        return isComplete;
    }
    
    /**
     * Even the most evil villain should pretty print his results.
     */
    private void printValidRoutes() {
        if (validRoutes.isEmpty()) {
            System.out.println("I am deeply in sorrow evil master,\n\nthere is no way for your sinister henchmen to travel all the places only once.");
        } else {
            System.out.println("Welcome evil master,\n\nyour sinister henchmen may travel one of the following routes:\n");
            validRoutes.forEach(r -> {
                if (!r.isEmpty()) {
                    System.out.print("You're henchman may ");
                    if (r.size() == 1) {
                        System.out.println(String.format("stay at %s.", r.get(0)));
                    } else {
                        System.out.print(String.format("travel from %s ", r.get(0)));
                        if (r.size() > 2) {
                            System.out.print(String.format("via %s", r.get(1)));
                            for (int i=2; i < r.size() - 2; i++) {
                                System.out.print(String.format(", %s", r.get(i)));
                            }
                            if (r.size() > 3) {
                                System.out.print(String.format(" and %s", r.get(r.size() - 2)));
                            }
                        }
                        System.out.println(String.format(" to %s.", r.get(r.size() - 1)));
                    }
                }
            });
            System.out.println("\nI hope this satisfies your needs, your Evilness");
        }
    }
}
