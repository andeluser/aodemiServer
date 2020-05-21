package dto;

import java.sql.Timestamp;

public class BattleBaseDTO {
	private String battle_id;
	private String player_id;
	private int sub_no;
	private int new_flg;
	private int yellow;
	private int black;
	private int red;
	private int blue;
	private int special_gage;
	private int special_stock;
	private int special_use;
	private int life;
	private String set_card;
	private int set_deck_no;
	private String cemetery;
	private String disappearance;
	private int sp;
	private int turn_up_sp;
	private int magic;
	private int divine;
	private Timestamp update_date;

	public String getBattle_id() {
		return battle_id;
	}
	public void setBattle_id(String battle_id) {
		this.battle_id = battle_id;
	}
	public int getSub_no() {
		return sub_no;
	}
	public void setSub_no(int sub_no) {
		this.sub_no = sub_no;
	}
	public String getPlayer_id() {
		return player_id;
	}
	public void setPlayer_id(String player_id) {
		this.player_id = player_id;
	}
	public int getYellow() {
		return yellow;
	}
	public void setYellow(int yellow) {
		this.yellow = yellow;
	}
	public int getBlack() {
		return black;
	}
	public void setBlack(int black) {
		this.black = black;
	}
	public int getRed() {
		return red;
	}
	public void setRed(int red) {
		this.red = red;
	}
	public int getBlue() {
		return blue;
	}
	public void setBlue(int blue) {
		this.blue = blue;
	}
	public int getSpecial_gage() {
		return special_gage;
	}
	public void setSpecial_gage(int special_gage) {
		this.special_gage = special_gage;
	}
	public int getSpecial_stock() {
		return special_stock;
	}
	public void setSpecial_stock(int special_stock) {
		this.special_stock = special_stock;
	}
	public int getSpecial_use() {
		return special_use;
	}
	public void setSpecial_use(int special_use) {
		this.special_use = special_use;
	}
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public String getSet_card() {
		return set_card;
	}
	public void setSet_card(String set_card) {
		this.set_card = set_card;
	}
	public int getSet_deck_no() {
		return set_deck_no;
	}
	public void setSet_deck_no(int set_deck_no) {
		this.set_deck_no = set_deck_no;
	}
	public String getCemetery() {
		return cemetery;
	}
	public void setCemetery(String cemetery) {
		this.cemetery = cemetery;
	}
	public String getDisappearance() {
		return disappearance;
	}
	public void setDisappearance(String disappearance) {
		this.disappearance = disappearance;
	}
	public int getSp() {
		return sp;
	}
	public void setSp(int sp) {
		this.sp = sp;
	}
	public int getMagic() {
		return magic;
	}
	public void setMagic(int magic) {
		this.magic = magic;
	}
	public int getDivine() {
		return divine;
	}
	public void setDivine(int divine) {
		this.divine = divine;
	}
	public Timestamp getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Timestamp update_date) {
		this.update_date = update_date;
	}
	public int getNew_flg() {
		return new_flg;
	}
	public void setNew_flg(int new_flg) {
		this.new_flg = new_flg;
	}
	public int getTurn_up_sp() {
		return turn_up_sp;
	}
	public void setTurn_up_sp(int turn_up_sp) {
		this.turn_up_sp = turn_up_sp;
	}

}
