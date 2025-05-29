/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.dream.game.network.clientpackets;

import com.dream.Config;
import com.dream.game.model.actor.instance.L2PcInstance;
import com.dream.game.network.serverpackets.KeyPacket;
import com.dream.game.network.serverpackets.L2GameServerPacket;
import com.protect.Spartan;
import com.protect.SpartanConfig;

public final class ProtocolVersion extends L2GameClientPacket
{
	private int _version;
	private String _hwidHdd = "NoHWID-HD";
	private String _hwidMac = "NoHWID-MAC";
	private String _hwidCPU = "NoHWID-CPU";

	@Override
	protected void readImpl()
	{
		_version = readD();

		if (_buf.remaining() > 260)
		{
			byte[] _data = new byte[260];
			readB(_data);
				_hwidHdd = readS();
				_hwidMac = readS();
				_hwidCPU = readS();
		}

	}

	@Override
	protected void runImpl()
	{
		if (_version == -2)
		{
			getClient().close((L2GameServerPacket) null);
		}
		if (Spartan.isEnabled())
		{
			if (_version < SpartanConfig.SPARTAN_PROTOCOL || _version > SpartanConfig.SPARTAN_PROTOCOL) {
				_log.info("Client: " + getClient().toString() + " -> Protocol Revision: " + _version + " is invalid.");
				getClient().sendPacket(new KeyPacket(getClient().enableCrypt(), 0));
				getClient().setProtocolOk(false);
				getClient().close((L2GameServerPacket) null);
			}
			else
			{
				L2PcInstance player;
				if (_hwidHdd.equals("NoGuard") && _hwidMac.equals("NoGuard") && _hwidCPU.equals("NoGuard"))
				{
					_log.info("HWID Status: No Client side dlls");
					getClient().close(new KeyPacket(_client.enableCrypt()));
				}

				switch (SpartanConfig.GET_CLIENT_HWID) {
					case 1: _client.setHWID(_hwidHdd);
						break;
					case 2: _client.setHWID(_hwidMac);
						break;
					case 3: _client.setHWID(_hwidCPU);
				}

				getClient().setProtocolOk(true);
				getClient().setProtocolVer(_version);
				getClient().sendPacket(new KeyPacket(getClient().enableCrypt(), 1));
				//_log.info("[SpartanGuard]: New Login -> Player:["+  getClient().getPlayerName() +"] - HWID:["+ _client.getHWID() +"]");
			}
		}
		else
		{
			if (_version < Config.MIN_PROTOCOL_REVISION || _version > Config.MAX_PROTOCOL_REVISION) {
				_log.info("Client: " + getClient().toString() + " -> Protocol Revision: " + _version + " is invalid.");
				getClient().sendPacket(new KeyPacket(getClient().enableCrypt(), 0));
				getClient().setProtocolOk(false);
				getClient().close((L2GameServerPacket) null);
			}
			else
			{

				getClient().setProtocolOk(true);
				getClient().setProtocolVer(_version);
				getClient().sendPacket(new KeyPacket(getClient().enableCrypt(), 1));
			}
		}
	}
}