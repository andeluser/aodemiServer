package dao;

import dto.BattleControllDTO;

public interface BattleControllDAO {

	BattleControllDTO getAllValue(String battleID) throws Exception;
	int insert(BattleControllDTO dto) throws Exception;
	int deleteBattle() throws Exception;
	int update(BattleControllDTO dto) throws Exception;
}
