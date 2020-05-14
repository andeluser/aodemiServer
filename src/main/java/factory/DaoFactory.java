package factory;

import dao.BattleBaseDAO;
import dao.BattleBaseDAOImpl;
import dao.BattleControllDAO;
import dao.BattleControllDAOImpl;
import dao.BattleDeckDAO;
import dao.BattleDeckDAOImpl;
import dao.BattleFieldDAO;
import dao.BattleFieldDAOImpl;
import dao.BattleShieldDAO;
import dao.BattleShieldDAOImpl;

public class DaoFactory {

	public BattleControllDAO createControllDAO() {
		return new BattleControllDAOImpl();
	}

	public BattleBaseDAO createBaseDAO() {
        return new BattleBaseDAOImpl();
    }

	public BattleFieldDAO createFieldDAO() {
		return new BattleFieldDAOImpl();
	}

	public BattleDeckDAO createDeckDAO() {
        return new BattleDeckDAOImpl();
    }

	public BattleShieldDAO createShieldDAO() {
        return new BattleShieldDAOImpl();
    }
}
