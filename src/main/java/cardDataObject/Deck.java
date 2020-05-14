package cardDataObject;

import java.util.ArrayList;

public class Deck {

	private String userId = "";

	private String life = "";

	private Special special = new Special();

	private ArrayList<Card> cardMap = new ArrayList<Card>();

	private ArrayList<Shield> shieldMap = new ArrayList<Shield>();

	public void setCardList( ArrayList<Card> card ) {
        this.cardMap = card;
	}

	public ArrayList<Card> getCardList() {
		return this.cardMap;
	}

	public void setShieldList( ArrayList<Shield> shield ) {
        this.shieldMap = shield;
	}

	public ArrayList<Shield> getShieldList() {
		return this.shieldMap;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLife() {
		return life;
	}

	public void setLife(String life) {
		this.life = life;
	}

	public Special getSpecial() {
		return special;
	}

	public void setSpecial(Special special) {
		this.special = special;
	}
}
