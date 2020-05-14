package dto;

import java.sql.Timestamp;

public class BattleShieldDTO {

	private String battle_id;
	private String player_id;
	private int shield_no;
	private int sub_no;
	private int new_flg;
	private String shield_id;
	private int shield_life;
	private int shield_select;
	private int shield_open;
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
	public int getShield_no() {
		return shield_no;
	}
	public void setShield_no(int shield_no) {
		this.shield_no = shield_no;
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
	public String getShield_id() {
		return shield_id;
	}
	public void setShield_id(String shield_id) {
		this.shield_id = shield_id;
	}
	public int getShield_life() {
		return shield_life;
	}
	public void setShield_life(int shield_life) {
		this.shield_life = shield_life;
	}
	public int getShield_select() {
		return shield_select;
	}
	public void setShield_select(int shield_select) {
		this.shield_select = shield_select;
	}
	public int getShield_open() {
		return shield_open;
	}
	public void setShield_open(int shield_open) {
		this.shield_open = shield_open;
	}
	public Timestamp getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Timestamp update_date) {
		this.update_date = update_date;
	}

}
