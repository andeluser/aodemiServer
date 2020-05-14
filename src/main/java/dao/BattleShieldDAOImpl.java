package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import dto.BattleShieldDTO;
import util.AodemiLogger;
import util.DBManeger;

public class BattleShieldDAOImpl implements BattleShieldDAO {

	public BattleShieldDTO getAllValue(String battleID, String playerId, int shieldNumber) throws Exception {

		BattleShieldDTO ret = new BattleShieldDTO();
		DBManeger accesser = new DBManeger();

    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;

    	try {

    		conn = accesser.getConnection();

	    	stmt = conn.createStatement();
			String sql = "SELECT * FROM BATTLE_SHIELD WHERE BATTLE_ID = '" + battleID + "' AND PLAYER_ID = '" + playerId + "' AND SHIELD_NO = '" + shieldNumber + "'";

			rs = stmt.executeQuery(sql);

	    	while (rs.next()) {
	    		ret.setBattle_id(rs.getString("BATTLE_ID"));
	    		ret.setPlayer_id(rs.getString("PLAYER_ID"));
	    		ret.setShield_no(rs.getInt("SHIELD_NO"));
	    		ret.setSub_no(rs.getInt("SUB_NO"));
	    		ret.setNew_flg(rs.getInt("NEW_FLG"));
	    		ret.setShield_id(rs.getString("SHIELD_ID"));
	    		ret.setShield_life(rs.getInt("SHIELD_LIFE"));
	    		ret.setShield_select(rs.getInt("SHIELD_SELECT"));
	    		ret.setShield_open(rs.getInt("SHIELD_OPEN"));
	    		ret.setUpdate_date(rs.getTimestamp("UPDATE_DATE"));
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

	public ArrayList<BattleShieldDTO> getAllList(String battleID, String playerId) throws Exception {

		ArrayList<BattleShieldDTO> list = new ArrayList<BattleShieldDTO>();

		DBManeger accesser = new DBManeger();

    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;

    	try {

    		conn = accesser.getConnection();

	    	stmt = conn.createStatement();
			String sql = "SELECT * FROM BATTLE_SHIELD WHERE BATTLE_ID = '" + battleID + "' AND PLAYER_ID = '" + playerId + "' ORDER BY SHIELD_NO";

	    	rs = stmt.executeQuery(sql);

	    	while (rs.next()) {
	    		BattleShieldDTO dto = new BattleShieldDTO();

	    		dto.setBattle_id(rs.getString("BATTLE_ID"));
	    		dto.setPlayer_id(rs.getString("PLAYER_ID"));
	    		dto.setShield_no(rs.getInt("SHIELD_NO"));
	    		dto.setSub_no(rs.getInt("SUB_NO"));
	    		dto.setNew_flg(rs.getInt("NEW_FLG"));
	    		dto.setShield_id(rs.getString("SHIELD_ID"));
	    		dto.setShield_life(rs.getInt("SHIELD_LIFE"));
	    		dto.setShield_select(rs.getInt("SHIELD_SELECT"));
	    		dto.setShield_open(rs.getInt("SHIELD_OPEN"));
	    		dto.setUpdate_date(rs.getTimestamp("UPDATE_DATE"));

	    		list.add(dto);
	    	}

			return list;

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

	public int insert(BattleShieldDTO dto) throws Exception {

		int ret = 0;

    	Statement stmt = null;
    	DBManeger accesser = new DBManeger();
    	Connection conn = null;

    	try {
    		conn = accesser.getConnection();
	     	stmt = conn.createStatement();

			String sql = "INSERT INTO BATTLE_SHIELD VALUES ("
					+ "'" + dto.getBattle_id() + "'"
					+ ", '" + dto.getPlayer_id() + "'"
					+ ", '" + dto.getShield_no() + "'"
					+ ", '" + dto.getSub_no() + "'"
					+ ", '" + dto.getNew_flg() + "'"
					+ ", '" + dto.getShield_id() + "'"
					+ ", '" + dto.getShield_life() + "'"
					+ ", '" + dto.getShield_select() + "'"
					+ ", '" + dto.getShield_open() + "'"
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

	public int deleteBattle(BattleShieldDTO dto) throws Exception {

		int ret = 0;
    	Statement stmt = null;
    	DBManeger accesser = new DBManeger();
    	Connection conn = null;

    	try {
    		conn = accesser.getConnection();
	     	stmt = conn.createStatement();

			String sql = "DELETE FROM BATTLE_SHIELD WHERE PLAYER_ID = '" + dto.getPlayer_id() + "'";

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

	public int update(BattleShieldDTO dto) throws Exception {

		int ret = 0;
		DBManeger accesser = new DBManeger();
		Connection conn = null;
    	Statement stmt = null;

    	try {

    		conn = accesser.getConnection();

	    	stmt = conn.createStatement();
			String sql = "UPDATE BATTLE_SHIELD SET "
//					+ ", PLAYER_ID = '" + dto.getPlayer_id() + "'"
//					+ ", SHIELD_NO = '" + dto.getShield_no() + "'"
//					+ ", SUB_NO = '" + dto.getSub_no() + "'"
//					+ ", NEW_FLG = '" + dto.getNew_flg() + "'"
					+ "SHIELD_ID = '" + dto.getShield_id() + "'"
					+ ", SHIELD_LIFE = '" + dto.getShield_life() + "'"
					+ ", SHIELD_SELECT = '" + dto.getShield_select() + "'"
					+ ", SHIELD_OPEN = '" + dto.getShield_open() + "'"
					+ ", UPDATE_DATE = CURRENT_TIMESTAMP"
					+ " WHERE BATTLE_ID = '" + dto.getBattle_id() + "' AND PLAYER_ID = '" + dto.getPlayer_id()+ "' AND SHIELD_NO ='" + dto.getShield_no() + "'";

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
