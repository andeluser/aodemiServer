package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import dto.BattleControllDTO;
import util.AodemiLogger;
import util.DBManeger;

public class BattleControllDAOImpl implements BattleControllDAO {

	public BattleControllDTO getAllValue(String battleID) throws Exception {

		BattleControllDTO ret = new BattleControllDTO();
		DBManeger accesser = new DBManeger();

    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;

    	try {

    		conn = accesser.getConnection();

	    	stmt = conn.createStatement();
			String sql = "SELECT * FROM BATTLE_CONTROLL WHERE BATTLE_ID = '" + battleID + "'";

	    	rs = stmt.executeQuery(sql);

	    	while (rs.next()) {
	    		ret.setBattle_id(rs.getString("BATTLE_ID"));
	    		ret.setSub_no(rs.getInt("SUB_NO"));
	    		ret.setNew_flg(rs.getInt("NEW_FLG"));
	    		ret.setTurncount(rs.getInt("TURNCOUNT"));
	    		ret.setOut(rs.getString("OUT"));
	    		ret.setNext_action(rs.getString("NEXT_ACTION"));
	    		ret.setPlayer_id_1(rs.getString("PLAYER_ID_1"));
	    		ret.setSpecial_color_1(rs.getString("SPECIAL_COLOR_1"));
	    		ret.setSpecial_id_1(rs.getString("SPECIAL_ID_1"));
	    		ret.setLife_1(rs.getInt("LIFE_1"));
	    		ret.setDeck_no_1(rs.getInt("DECK_NO_1"));
	    		ret.setPlayer_id_2(rs.getString("PLAYER_ID_2"));
	    		ret.setSpecial_color_2(rs.getString("SPECIAL_COLOR_2"));
	    		ret.setSpecial_id_2(rs.getString("SPECIAL_ID_2"));
	    		ret.setLife_2(rs.getInt("LIFE_2"));
	    		ret.setDeck_no_2(rs.getInt("DECK_NO_2"));
	    		ret.setPhase(rs.getString("PHASE"));
	    		ret.setStatus_1(rs.getString("STATUS_1"));
	    		ret.setStatus_2(rs.getString("STATUS_2"));
	    		ret.setPlayer_input_1(rs.getString("PLAYER_INPUT_1"));
	    		ret.setPlayer_input_2(rs.getString("PLAYER_INPUT_2"));
	    		ret.setPlayer_output_1(rs.getString("PLAYER_OUTPUT_1"));
	    		ret.setPlayer_output_2(rs.getString("PLAYER_OUTPUT_2"));
	    		ret.setPlayer_revival_1(rs.getString("PLAYER_REVIVAL_1"));
	    		ret.setPlayer_revival_2(rs.getString("PLAYER_REVIVAL_2"));
	    		ret.setPlayer_surrend_1(rs.getInt("PLAYER_SURREND_1"));
	    		ret.setPlayer_surrend_2(rs.getInt("PLAYER_SURREND_2"));
	    		ret.setNext_special_player_1(rs.getString("NEXT_SPECIAL_PLAYER_1"));
	    		ret.setNext_special_status_1(rs.getString("NEXT_SPECIAL_STATUS_1"));
	    		ret.setNext_special_player_2(rs.getString("NEXT_SPECIAL_PLAYER_2"));
	    		ret.setNext_special_status_2(rs.getString("NEXT_SPECIAL_STATUS_2"));
	    		ret.setNext_open_player_1(rs.getString("NEXT_OPEN_PLAYER_1"));
	    		ret.setNext_open_status_1(rs.getString("NEXT_OPEN_STATUS_1"));
	    		ret.setNext_open_player_2(rs.getString("NEXT_OPEN_PLAYER_2"));
	    		ret.setNext_open_status_2(rs.getString("NEXT_OPEN_STATUS_2"));
	    	}

			return ret;

    	} finally {
    		if (rs != null) {
    			rs.close();
    		}

    		if (stmt != null) {
    			stmt.close();
    		}

    		if (conn != null) {
    			conn.close();
    		}

    	}
	}

	public int insert(BattleControllDTO dto) throws Exception {

		int ret = 0;

		Statement stmt = null;
    	DBManeger accesser = new DBManeger();
    	Connection conn = null;

    	try {
    		conn = accesser.getConnection();
	     	stmt = conn.createStatement();

			String sql = "INSERT INTO BATTLE_CONTROLL VALUES ("
					+ "'" + dto.getBattle_id() + "'"
					+ ", '" + dto.getSub_no() + "'"
					+ ", '" + dto.getNew_flg() + "'"
					+ ", '" + dto.getTurncount() + "'"
					+ ", '" + dto.getOut() + "'"
					+ ", '" + dto.getNext_action() + "'"
					+ ", '" + dto.getPlayer_id_1() + "'"
					+ ", '" + dto.getSpecial_color_1() + "'"
					+ ", '" + dto.getSpecial_id_1() + "'"
					+ ", '" + dto.getLife_1() + "'"
					+ ", '" + dto.getDeck_no_1() + "'"
					+ ", '" + dto.getPlayer_id_2() + "'"
					+ ", '" + dto.getSpecial_color_2() + "'"
					+ ", '" + dto.getSpecial_id_2() + "'"
					+ ", '" + dto.getLife_2() + "'"
					+ ", '" + dto.getDeck_no_2() + "'"
					+ ", '" + dto.getPhase() + "'"
					+ ", '" + dto.getStatus_1() + "'"
					+ ", '" + dto.getStatus_2() + "'"
					+ ", '" + dto.getPlayer_input_1() + "'"
					+ ", '" + dto.getPlayer_input_2() + "'"
					+ ", '" + dto.getPlayer_output_1() + "'"
					+ ", '" + dto.getPlayer_output_2() + "'"
					+ ", '" + dto.getPlayer_revival_1() + "'"
					+ ", '" + dto.getPlayer_revival_2() + "'"
					+ ", '" + dto.getPlayer_surrend_1() + "'"
					+ ", '" + dto.getPlayer_surrend_2() + "'"
					+ ", '" + dto.getNext_special_player_1() + "'"
					+ ", '" + dto.getNext_special_status_1() + "'"
					+ ", '" + dto.getNext_special_player_2() + "'"
					+ ", '" + dto.getNext_special_status_2() + "'"
					+ ", '" + dto.getNext_open_player_1() + "'"
					+ ", '" + dto.getNext_open_status_1() + "'"
					+ ", '" + dto.getNext_open_player_2() + "'"
					+ ", '" + dto.getNext_open_status_2() + "'"
					+ ", CURRENT_TIMESTAMP)";
			ret = stmt.executeUpdate(sql);

			conn.commit();

    	} finally {
    		if (stmt != null) {
    			stmt.close();
    		}

    		if (conn != null) {
    			conn.close();
    		}
    	}

    	return ret;
	}

	public int deleteBattle() throws Exception {

		int ret = 0;
    	Statement stmt = null;
    	DBManeger accesser = new DBManeger();
    	Connection conn = null;

    	try {
    		conn = accesser.getConnection();
	     	stmt = conn.createStatement();

			String sql = "DELETE FROM BATTLE_CONTROLL";

			ret = stmt.executeUpdate(sql);

			conn.commit();

    	} finally {
    		if (stmt != null) {
    			stmt.close();
    		}

    		if (conn != null) {
    			conn.close();
    		}
    	}

    	return ret;
	}

	public int update(BattleControllDTO dto) throws Exception {

		int ret = 0;
		DBManeger accesser = new DBManeger();
    	Connection conn = accesser.getConnection();

    	Statement stmt = null;

    	try {

	    	stmt = conn.createStatement();
			String sql = "UPDATE BATTLE_CONTROLL SET "
	//				+ ", BATTLE_ID = '" + dto.getBattle_id() + "'"
	//				+ ", SUB_NO = '" + dto.getSub_no() + "'"
	//				+ ", NEW_FLG = '" + dto.getNew_flg() + "'"
					+ "TURNCOUNT = '" + dto.getTurncount() + "'"
					+ ", OUT = '" + dto.getOut() + "'"
					+ ", NEXT_ACTION = '" + dto.getNext_action() + "'"
					+ ", PLAYER_ID_1 = '" + dto.getPlayer_id_1() + "'"
					+ ", SPECIAL_COLOR_1 = '" + dto.getSpecial_color_1() + "'"
					+ ", SPECIAL_ID_1 = '" + dto.getSpecial_id_1() + "'"
					+ ", LIFE_1 = '" + dto.getLife_1() + "'"
					+ ", DECK_NO_1 = '" + dto.getDeck_no_1() + "'"
					+ ", PLAYER_ID_2 = '" + dto.getPlayer_id_2() + "'"
					+ ", SPECIAL_COLOR_2 = '" + dto.getSpecial_color_2() + "'"
					+ ", SPECIAL_ID_2 = '" + dto.getSpecial_id_2() + "'"
					+ ", LIFE_2 = '" + dto.getLife_2() + "'"
					+ ", DECK_NO_2 = '" + dto.getDeck_no_2() + "'"
					+ ", PHASE = '" + dto.getPhase() + "'"
					+ ", STATUS_1 = '" + dto.getStatus_1() + "'"
					+ ", STATUS_2 = '" + dto.getStatus_2() + "'"
					+ ", PLAYER_INPUT_1 = '" + dto.getPlayer_input_1() + "'"
					+ ", PLAYER_INPUT_2 = '" + dto.getPlayer_input_2() + "'"
					+ ", PLAYER_OUTPUT_1 = '" + dto.getPlayer_output_1() + "'"
					+ ", PLAYER_OUTPUT_2 = '" + dto.getPlayer_output_2() + "'"
					+ ", PLAYER_REVIVAL_1 = '" + dto.getPlayer_revival_1() + "'"
					+ ", PLAYER_REVIVAL_2 = '" + dto.getPlayer_revival_2() + "'"
					+ ", PLAYER_SURREND_1 = '" + dto.getPlayer_surrend_1() + "'"
					+ ", PLAYER_SURREND_2 = '" + dto.getPlayer_surrend_2() + "'"
					+ ", NEXT_SPECIAL_PLAYER_1 = '" + dto.getNext_special_player_1() + "'"
					+ ", NEXT_SPECIAL_STATUS_1 = '" + dto.getNext_special_status_1() + "'"
					+ ", NEXT_SPECIAL_PLAYER_2 = '" + dto.getNext_special_player_2() + "'"
					+ ", NEXT_SPECIAL_STATUS_2 = '" + dto.getNext_special_status_2() + "'"
					+ ", NEXT_OPEN_PLAYER_1 = '" + dto.getNext_open_player_1() + "'"
					+ ", NEXT_OPEN_STATUS_1 = '" + dto.getNext_open_status_1() + "'"
					+ ", NEXT_OPEN_PLAYER_2 = '" + dto.getNext_open_player_2() + "'"
					+ ", NEXT_OPEN_STATUS_2 = '" + dto.getNext_open_status_2() + "'"
					+ ", UPDATE_DATE = CURRENT_TIMESTAMP"
					+ " WHERE BATTLE_ID = '" + dto.getBattle_id() + "'";

			AodemiLogger.writeLogOnly(sql);

	    	ret = stmt.executeUpdate(sql);

	    	conn.commit();

    	} finally {
    		if (stmt != null) {
    			stmt.close();
    		}

    		if (conn != null) {
    			conn.close();
    		}
    	}

    	return ret;
	}
}
