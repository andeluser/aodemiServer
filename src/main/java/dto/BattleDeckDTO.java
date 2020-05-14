package dto;

import java.sql.Timestamp;

public class BattleDeckDTO {
	private String battle_id;
	private String player_id;
	private int deck_no;
	private int sub_no;
	private int new_flg;
	private String card_id;
	private int card_lock;
	private int card_out;
	private Timestamp update_date;
	public String getBattle_id() {
		return battle_id;
	}
	public void setBattle_id(String battle_id) {
		this.battle_id = battle_id;
	}
	public String getPlayer_id() {
		return player_id;
	}
	public void setPlayer_id(String player_id) {
		this.player_id = player_id;
	}
	public int getDeck_no() {
		return deck_no;
	}
	public void setDeck_no(int deck_no) {
		this.deck_no = deck_no;
	}
	public int getSub_no() {
		return sub_no;
	}
	public void setSub_no(int sub_no) {
		this.sub_no = sub_no;
	}
	public int getNew_flg() {
		return new_flg;
	}
	public void setNew_flg(int new_flg) {
		this.new_flg = new_flg;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public int getCard_lock() {
		return card_lock;
	}
	public void setCard_lock(int card_lock) {
		this.card_lock = card_lock;
	}
	public int getCard_out() {
		return card_out;
	}
	public void setCard_out(int card_out) {
		this.card_out = card_out;
	}
	public Timestamp getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Timestamp update_date) {
		this.update_date = update_date;
	}

}
