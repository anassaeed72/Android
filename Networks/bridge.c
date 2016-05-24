/* 
 * File: bridge.c 
 * ==============
 *
 * This is a rough skeleton of bridge.c which only lists the subroutines
 * you HAVE to implement as the simulator makes use of these
 * subrouines. Complete this file by filling in these subroutines and
 * writing your own supplemental subroutines that you might need
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include "bridge.h"

void *AllocateBridge()
{ 
	/* Do not make any changes to this subroutine */
	return ((void*)malloc(sizeof(Bridge)));
}


int getBridgeNumPorts(Bridge* bridge) {
	return bridge->numPorts;
}

void intializeBPDU(int bridgeID, Bridge* bridge) {
	/* Adjust the initial BPDU */
	bridge->transmitBPDU->data[0] = bridgeID;
	bridge->transmitBPDU->data[1] = 0;
	bridge->transmitBPDU->data[2] = bridgeID;
	bridge->transmitBPDU->dstAddress = ALLBRIDGES;
	bridge->transmitBPDU->DSAP = BRIDGEPROTOCOL;
	bridge->transmitBPDU->SSAP = BRIDGEPROTOCOL;

}

void intilizeBridgeFields(int bridgeID, int numPorts, Bridge* bridge,
		int* MACaddresses) {
	bridge->transmitBPDU = malloc(sizeof(MACFrame));
	bridge->bridgeID = bridgeID;
	bridge->numPorts = numPorts;
	bridge->MACaddresses = MACaddresses;
	bridge->rootId = -1;
	bridge->rootMac = -1;
	bridge->portState = malloc(numPorts*sizeof(PortState));
}

void makeAllPortsDesignated(int i, int numPorts, Bridge* bridge) {
	for (i = 0; i < numPorts; ++i)
		bridge->portState[i] = Designated;
}

void InitializeBridge(Bridge *bridge, int bridgeID, int numPorts,
		int
		       *MACaddresses, int ignore1, int ignore2, int ignore3)
{
	int i;
	intilizeBridgeFields(bridgeID, numPorts, bridge, MACaddresses);
	intializeBPDU(bridgeID, bridge);

	makeAllPortsDesignated(i, numPorts, bridge);
}

void SetRootPortAndBlock(int port, int rootMac, MACFrame* bestBPDU,
		Bridge* bridge) {
	//====================================================================================
	// Start/ Setting port which received best BPDU as root and blocking the previous Root
	//====================================================================================
	if (bestBPDU != NULL && CompareBPDU(bestBPDU, bridge->transmitBPDU) == 1) {
		if (bridge->rootId != -1)
			bridge->portState[bridge->rootId] = Blocked;

		bridge->rootId = port;
		bridge->transmitBPDU->data[0] = bestBPDU->data[0];
		bridge->transmitBPDU->data[1] = bestBPDU->data[1] + 1;
		bridge->portState[port] = Root;
		bridge->rootMac = rootMac;
	}
}

void SetPortsStatebasedOnBPDU(int port, MACQueueElement* head, Bridge* bridge) {
	//====================================================================================
	// Start/ Setting the state of the ports based on BPDU's received
	//====================================================================================
	while (head != NULL ) {
		if (head->frame->SSAP == BRIDGEPROTOCOL) {
			port = FindPort(bridge, head->MACaddress);
			// if a port has been blocked. No need to change it's state
			if (bridge->portState[port] == Designated) {
				// transmitBPDU < receiveBPDU
				if (CompareBPDU(head->frame, bridge->transmitBPDU) == 1) {
					bridge->portState[port] = Blocked;
				}

			}
		}

		head = head->next;
	}
}

int checkIfPortIsBlocked(Bridge* bridge, MACQueueElement* receiveQueue) {
	return bridge->portState[FindPort(bridge, receiveQueue->MACaddress)]
			== Blocked;
}

int isPortDesignated(int i, Bridge* bridge) {
	return bridge->portState[i] == Designated;
}

void transitMessage(int i, Bridge* bridge) {
	bridge->transmitBPDU->srcAddress = bridge->MACaddresses[i];
	bridge->transmitBPDU->data[3] = i;
	SendMACFrame(bridge->MACaddresses[i], *bridge->transmitBPDU);
}

void  bradcastMessgages( Bridge* bridge) {
	int i = 0;
	for (i = 0; i < bridge->numPorts; ++i) {
		if (isPortDesignated(i, bridge)) {
			transitMessage(i, bridge);
		}
	}
}

int Execute(Bridge *bridge, MACQueueElement *receiveQueue, unsigned long step)
{

	int i;
	int port = -1;
	int rootMac;
	MACQueueElement* head = receiveQueue;
	MACFrame* bestBPDU = NULL;

	bradcastMessgages(bridge);

	// First traverse the linkedList receiveQueue and process BPDU's and data
	if (receiveQueue!= NULL)
	bestBPDU = receiveQueue->frame;
	else
		return 0;

	port = FindPort(bridge, receiveQueue->MACaddress);
	rootMac = receiveQueue->MACaddress;

	while(receiveQueue != NULL)
	{
		if (checkIfPortIsBlocked(bridge, receiveQueue)){
			receiveQueue = receiveQueue->next;
			continue;
		}

		if (receiveQueue->frame->SSAP == BRIDGEPROTOCOL && receiveQueue->frame->DSAP == BRIDGEPROTOCOL){
			if (CompareBPDU(receiveQueue->frame, bestBPDU) == 1){
				bestBPDU = receiveQueue->frame;
				port = FindPort(bridge, receiveQueue->MACaddress);
				rootMac = receiveQueue->MACaddress;
			}
		}
		// First learn
//====================================================================================
// Handle all the packets that are not BPDU
//====================================================================================

		else if (receiveQueue->frame->SSAP == IP && receiveQueue->frame->DSAP == IP)
		{
			int srcmac = receiveQueue->frame->srcAddress;
			int dst = receiveQueue->frame->dstAddress;
			int portId = FindPort(bridge, receiveQueue->MACaddress);
			int entry = FindFDBEntry(bridge, srcmac);
			if (FindFDBEntry(bridge, srcmac) == -1){
				PutFDBEntry(bridge, portId, srcmac);
			}
			entry = FindFDBEntry(bridge, dst);

			if (entry == -1 || dst == BROADCAST)
			{
				for (i = 0; i < bridge->numPorts; ++i){
					if (portId == i || bridge->portState[i] == Blocked)
						continue;
					else
					{
						SendMACFrame(bridge->MACaddresses[i], *receiveQueue->frame);
					}
				}
			}
			else
			{
				if (portId != entry)
					SendMACFrame(bridge->MACaddresses[entry], *receiveQueue->frame);
			}
		}
		receiveQueue = receiveQueue->next;
	}


	SetRootPortAndBlock(port, rootMac, bestBPDU, bridge);
	SetPortsStatebasedOnBPDU(port, head, bridge);


	return 0;
}



int *GetFDBEntry (Bridge *bridge, int MACaddress)
{ 
	int* retval = malloc(getBridgeNumPorts(bridge) * sizeof(int));
	int i = 0;
	for (i = 0; i < getBridgeNumPorts(bridge); i++)
		retval[i] = 0;

	i = FindFDBEntry(bridge, MACaddress);
	if (i != -1)
		retval[i] = 1;

	return retval;
}


int GetRootPort (Bridge *bridge)
{
	return bridge->rootMac;
}

int isBridgeBlocking(int MACaddress, Bridge* bridge) {
	return bridge->portState[FindPort(bridge, MACaddress)] == Blocked;
}


PortStateType GetPortState (Bridge *bridge, int MACaddress)
{
	if (isBridgeBlocking(MACaddress, bridge))
		return Blocking;
	else
		return Forwarding;
	PortStateType temp;
	return temp;
}


MACFrame *GetConfigBPDUatPort (Bridge *bridge, int MACaddress)
{
	bridge->transmitBPDU->data[3] = FindPort(bridge, MACaddress);
	return bridge->transmitBPDU;
}

int levelOneComparisonTrue(MACFrame* frame1, MACFrame* frame2) {
	return frame1->data[0] < frame2->data[0];
}

int levelOneComparisionFalse(MACFrame* frame2, MACFrame* frame1) {
	return frame2->data[0] < frame1->data[0];
}

int levelTwoComparisionTrue(MACFrame* frame1, MACFrame* frame2) {
	return frame1->data[1] < frame2->data[1];
}

int levelTwoComaprisionFalse(MACFrame* frame2, MACFrame* frame1) {
	return frame2->data[1] < frame1->data[1];
}

int levelThreeComparisionTrue(MACFrame* frame1, MACFrame* frame2) {
	return frame1->data[2] < frame2->data[2];
}
int levelThreeComparisionFalse(MACFrame* frame2, MACFrame* frame1) {
	return frame2->data[2] < frame1->data[2];
}

int levelFourComparisonTrue(MACFrame* frame1, MACFrame* frame2) {
	return frame1->data[3] < frame2->data[3];
}

int levelFourComparionsFalse(MACFrame* frame2, MACFrame* frame1) {
	return frame2->data[3] < frame1->data[3];
}

int levelOneComparision(MACFrame* frame2, MACFrame* frame1) {
	if (levelOneComparisonTrue(frame1, frame2))
		return 1;
	else if (levelOneComparisionFalse(frame2, frame1))
		return 0;
	return 2;
}


int levelTwoComparision(MACFrame* frame2, MACFrame* frame1) {
	if (levelTwoComparisionTrue(frame1, frame2))
			return 1;
		else if (levelTwoComaprisionFalse(frame2, frame1))
			return 0;
	return 2;

}

int levelThreeComparision(MACFrame* frame2, MACFrame* frame1) {
	if (levelThreeComparisionTrue(frame1, frame2))
			return 1;
		else if (levelThreeComparisionFalse(frame2, frame1))
			return 0;
	return 2;
}
int levelFourComparision(MACFrame* frame2, MACFrame* frame1) {
	if (levelFourComparisonTrue(frame1, frame2))
			return 1;
		else if (levelFourComparionsFalse(frame2, frame1))
			return 0;

	return 2;
}

int CompareBPDU(MACFrame* frame1, MACFrame* frame2)
{

	int result = levelOneComparision(frame1,frame2);
	if (result == 1 || result == 0)
		return result;

	result = levelTwoComparision(frame1,frame2);
		if (result == 1 || result == 0)
			return result;

	result = levelThreeComparision(frame1,frame2);
		if (result == 1 || result == 0)
			return result;

	result = levelFourComparision(frame1,frame2);
		if (result == 1 || result == 0)
			return result;
	return 0;

}

int bridgeMacAddressFound(int i, int MACaddress, Bridge* bridge) {
	return bridge->MACaddresses[i] == MACaddress;
}

int FindPort(Bridge* bridge, int MACaddress)
{
	int i = 0;
	for (i = 0; i < getBridgeNumPorts(bridge); i++)
	{
		if (bridgeMacAddressFound(i, MACaddress, bridge))
			return i;
	}

	return -1;
}

void setBridgePort(int port, Bridge* bridge) {
	bridge->FDB[bridge->numFDB].port = port;
}

void setBridgeStation(int MACaddress, Bridge* bridge) {
	bridge->FDB[bridge->numFDB].station = MACaddress;
}

void increaseBridgeNumFDB(Bridge* bridge) {
	bridge->numFDB++;
}


void PutFDBEntry(Bridge* bridge, int port, int MACaddress)
{
	setBridgePort(port, bridge);
	setBridgeStation(MACaddress, bridge);
	increaseBridgeNumFDB(bridge);
}

int FBEntryFound(int i, int MACaddress, Bridge* bridge) {
	return bridge->FDB[i].station == MACaddress;
}

int getBridgeNumFDB(Bridge* bridge) {
	return bridge->numFDB;
}

int FindFDBEntry(Bridge* bridge, int MACaddress)
{
	int i = 0;
	for (i = 0; i < getBridgeNumFDB(bridge); i++)
	{
		if (FBEntryFound(i, MACaddress, bridge))
			return bridge->FDB[i].port;
	}
	return -1;
}

void BridgeCleanup(Bridge* bridge)
{
	if (bridge != NULL)
		return;
	free(bridge->transmitBPDU);
	free(bridge->portState);
}
