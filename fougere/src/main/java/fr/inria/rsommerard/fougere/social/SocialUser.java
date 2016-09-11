package fr.inria.rsommerard.fougere.social;

/**
 * Created by romain on 06/09/16.
 */
public class SocialUser {

    private String identifier;

    public SocialUser(final String identifier) {
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
