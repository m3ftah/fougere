package fr.inria.rsommerard.fougere.contextual;

/**
 * Created by romain on 06/09/16.
 */
public class ContextualUser {

    private String identifier;

    public ContextualUser(final String identifier) {
        this.identifier = identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier(final String identifier) {
        return this.identifier;
    }

    @Override
    public String toString() {
        return "{\"identifier\":\"" + this.identifier + "\"}";
    }
}
