package dto;

import java.sql.Timestamp;

public class BattleDeckDTO {
	private String battle_id;
	private String player_id;
	private int deck_no;
	private int sub_no;
	private int new_flg;
	private String card_id;
	private String color;
	private String card_type;
	private String card_type1;
	private String card_type2;
	private int level;
	private int frm;
	private int hp;
	private int atk;
	private int def;
	private int agi;
	private int rng;
	private int card_lock;
	private int card_out;
	private int open_skill;
	private int start_skill;
	private int close_skill;
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
	public String getCard_type() {
		return card_type;
	}
	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}
	public String getCard_type1() {
		return card_type1;
	}
	public void setCard_type1(String card_type1) {
		this.card_type1 = card_type1;
	}
	public String getCard_type2() {
		return card_type2;
	}
	public void setCard_type2(String card_type2) {
		this.card_type2 = card_type2;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getFrm() {
		return frm;
	}
	public void setFrm(int frm) {
		this.frm = frm;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getAtk() {
		return atk;
	}
	public void setAtk(int atk) {
		this.atk = atk;
	}
	public int getDef() {
		return def;
	}
	public void setDef(int def) {
		this.def = def;
	}
	public int getAgi() {
		return agi;
	}
	public void setAgi(int agi) {
		this.agi = agi;
	}
	public int getRng() {
		return rng;
	}
	public void setRng(int rng) {
		this.rng = rng;
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
	public int getOpen_skill() {
		return open_skill;
	}
	public void setOpen_skill(int open_skill) {
		this.open_skill = open_skill;
	}
	public int getStart_skill() {
		return start_skill;
	}
	public void setStart_skill(int start_skill) {
		this.start_skill = start_skill;
	}
	public int getClose_skill() {
		return close_skill;
	}
	public void setClose_skill(int close_skill) {
		this.close_skill = close_skill;
	}
	public Timestamp getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Timestamp update_date) {
		this.update_date = update_date;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

}
