package dto;

import java.sql.Timestamp;

public class BattleFieldDTO {
	private String battle_id;
	private String player_id;
	private int field_no;
	private int sub_no;
	private int new_flg;
	private String card_id;
	private int close;
	private int close_number;
	private int open_close_number;
	private int close_skill;
	private int start_action;
	private int action;
	private String color;
	private String type1;
	private String type2;
	private int permanent_hp;
	private int turn_hp;
	private int base_hp;
	private int cur_hp;
	private int permanent_level;
	private int turn_level;
	private int cur_level;
	private int permanent_atk;
	private int turn_atk;
	private int cur_atk;
	private int permanent_def;
	private int turn_def;
	private int cur_def;
	private int permanent_speed;
	private int turn_speed;
	private int cur_speed;
	private int permanent_range;
	private int turn_range;
	private int cur_range;
	private int deck_no;
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
	public int getField_no() {
		return field_no;
	}
	public void setField_no(int field_no) {
		this.field_no = field_no;
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
	public int getClose() {
		return close;
	}
	public void setClose(int close) {
		this.close = close;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public int getPermanent_hp() {
		return permanent_hp;
	}
	public void setPermanent_hp(int permanent_hp) {
		this.permanent_hp = permanent_hp;
	}
	public int getTurn_hp() {
		return turn_hp;
	}
	public void setTurn_hp(int turn_hp) {
		this.turn_hp = turn_hp;
	}
	public int getBase_hp() {
		return base_hp;
	}
	public void setBase_hp(int base_hp) {
		this.base_hp = base_hp;
	}
	public int getCur_hp() {
		return cur_hp;
	}
	public void setCur_hp(int cur_hp) {
		this.cur_hp = cur_hp;
	}
	public int getPermanent_level() {
		return permanent_level;
	}
	public void setPermanent_level(int permanent_level) {
		this.permanent_level = permanent_level;
	}
	public int getTurn_level() {
		return turn_level;
	}
	public void setTurn_level(int turn_level) {
		this.turn_level = turn_level;
	}
	public int getCur_level() {
		return cur_level;
	}
	public void setCur_level(int cur_level) {
		this.cur_level = cur_level;
	}
	public int getPermanent_atk() {
		return permanent_atk;
	}
	public void setPermanent_atk(int permanent_atk) {
		this.permanent_atk = permanent_atk;
	}
	public int getTurn_atk() {
		return turn_atk;
	}
	public void setTurn_atk(int turn_atk) {
		this.turn_atk = turn_atk;
	}
	public int getCur_atk() {
		return cur_atk;
	}
	public void setCur_atk(int cur_atk) {
		this.cur_atk = cur_atk;
	}
	public int getPermanent_def() {
		return permanent_def;
	}
	public void setPermanent_def(int permanent_def) {
		this.permanent_def = permanent_def;
	}
	public int getTurn_def() {
		return turn_def;
	}
	public void setTurn_def(int turn_def) {
		this.turn_def = turn_def;
	}
	public int getCur_def() {
		return cur_def;
	}
	public void setCur_def(int cur_def) {
		this.cur_def = cur_def;
	}
	public int getPermanent_speed() {
		return permanent_speed;
	}
	public void setPermanent_speed(int permanent_speed) {
		this.permanent_speed = permanent_speed;
	}
	public int getTurn_speed() {
		return turn_speed;
	}
	public void setTurn_speed(int turn_speed) {
		this.turn_speed = turn_speed;
	}
	public int getCur_speed() {
		return cur_speed;
	}
	public void setCur_speed(int cur_speed) {
		this.cur_speed = cur_speed;
	}
	public int getPermanent_range() {
		return permanent_range;
	}
	public void setPermanent_range(int permanent_range) {
		this.permanent_range = permanent_range;
	}
	public int getTurn_range() {
		return turn_range;
	}
	public void setTurn_range(int turn_range) {
		this.turn_range = turn_range;
	}
	public int getCur_range() {
		return cur_range;
	}
	public void setCur_range(int cur_range) {
		this.cur_range = cur_range;
	}
	public int getDeck_no() {
		return deck_no;
	}
	public void setDeck_no(int deck_no) {
		this.deck_no = deck_no;
	}
	public Timestamp getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Timestamp update_date) {
		this.update_date = update_date;
	}
	public int getStart_action() {
		return start_action;
	}
	public void setStart_action(int start_action) {
		this.start_action = start_action;
	}
	public int getClose_number() {
		return close_number;
	}
	public void setClose_number(int close_number) {
		this.close_number = close_number;
	}
	public int getClose_skill() {
		return close_skill;
	}
	public void setClose_skill(int close_skill) {
		this.close_skill = close_skill;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getType1() {
		return type1;
	}
	public void setType1(String type1) {
		this.type1 = type1;
	}
	public String getType2() {
		return type2;
	}
	public void setType2(String type2) {
		this.type2 = type2;
	}
	public int getOpen_close_number() {
		return open_close_number;
	}
	public void setOpen_close_number(int open_close_number) {
		this.open_close_number = open_close_number;
	}


}
