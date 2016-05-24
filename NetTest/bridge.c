#include <stdio.h>
#include <stdlib.h>
#include "bridge.h"

void *AllocateBridge()
{ 
	return ((void*)malloc(sizeof(Bridge)));
}
int l1t(MACFrame* frame1, MACFrame* frame2) {
	return frame1->data[0] < frame2->data[0];
}

int l1f(MACFrame* frame2, MACFrame* frame1) {
	return frame2->data[0] < frame1->data[0];
}

int l2t(MACFrame* frame1, MACFrame* frame2) {
	return frame1->data[1] < frame2->data[1];
}

int l2f(MACFrame* frame2, MACFrame* frame1) {
	return frame2->data[1] < frame1->data[1];
}

int l3t(MACFrame* frame1, MACFrame* frame2) {
	return frame1->data[2] < frame2->data[2];
}

int l3f(MACFrame* frame2, MACFrame* frame1) {
	return frame2->data[2] < frame1->data[2];
}

int l4t(MACFrame* frame1, MACFrame* frame2) {
	return frame1->data[3] < frame2->data[3];
}

int l4f(MACFrame* frame2, MACFrame* frame1) {
	return frame2->data[3] < frame1->data[3];
}
int getBridgeNumFDB(Bridge* bridge) {
	return bridge->numFDB;
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
int FBEntryFound(int i, int MACaddress, Bridge* bridge) {
	return bridge->FDB[i].station == MACaddress;
}
int bridgeMacAddressFound(int i, int MACaddress, Bridge* bridge) {
	return bridge->MACaddresses[i] == MACaddress;
}
int getBridgeNumPorts(Bridge* bridge) {
	return bridge->numPorts;
}

int isBridgeBlocking(int MACaddress, Bridge* bridge) {
	return bridge->portState[FindPort(bridge, MACaddress)] == Blocked;
}
void intializeBPDU(int bridgeID, Bridge* bridge) {
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

void makeAllPortsDesignated(int numPorts, Bridge* bridge) {
	int i = 0;
	for (i = 0; i < numPorts; ++i)
		bridge->portState[i] = Designated;
}

void InitializeBridge(Bridge *bridge, int bridgeID, int numPorts,
		int
		       *MACaddresses, int ignore1, int ignore2, int ignore3)
{
	intilizeBridgeFields(bridgeID, numPorts, bridge, MACaddresses);
	intializeBPDU(bridgeID, bridge);

	makeAllPortsDesignated( numPorts, bridge);
}

int ifBPDUIsBest(MACQueueElement* head, Bridge* bridge) {
	return CompareBPDU(head->frame, bridge->transmitBPDU) == 1;
}

int isBridgeDesignated(int port, Bridge* bridge) {
	return bridge->portState[port] == Designated;
}
int isFrameBridgeType(MACQueueElement* head){
	return head->frame->SSAP == BRIDGEPROTOCOL;
}
void setPortsOnBPDU(int port, MACQueueElement* head, Bridge* bridge) {
	while (head != NULL ) {
		if (isFrameBridgeType(head)) {
			port = FindPort(bridge, head->MACaddress);
			if (isBridgeDesignated(port, bridge)) {
				if (ifBPDUIsBest(head, bridge))
					bridge->portState[port] = Blocked;
			}
		}
		head = head->next;
	}
}

void setBridgeDetails(int port, int rootMac, Bridge* bridge, MACFrame* bestBPDU) {
	bridge->rootId = port;
	bridge->transmitBPDU->data[0] = bestBPDU->data[0];
	bridge->transmitBPDU->data[1] = bestBPDU->data[1] + 1;
	bridge->portState[port] = Root;
	bridge->rootMac = rootMac;
}

void setRootPort(int port, int rootMac, MACFrame* bestBPDU, Bridge* bridge) {
	if (bestBPDU != NULL && CompareBPDU(bestBPDU, bridge->transmitBPDU) == 1) {
		if (bridge->rootId != -1)
			bridge->portState[bridge->rootId] = Blocked;
		setBridgeDetails(port, rootMac, bridge, bestBPDU);
	}
}

void broadcastMessage(Bridge* bridge) {
	int i;
	for (i = 0; i < bridge->numPorts; ++i) {
		if (bridge->portState[i] == Designated) {
			bridge->transmitBPDU->srcAddress = bridge->MACaddresses[i];
			bridge->transmitBPDU->data[3] = i;
			SendMACFrame(bridge->MACaddresses[i], *bridge->transmitBPDU);
		}
	}
}

int ownPort(int portId, int i) {
	return portId == i;
}

int isBridgeBlocked(int i, Bridge* bridge) {
	return bridge->portState[i] == Blocked;
}


int Execute(Bridge *bridge, MACQueueElement *receiveQueue, unsigned long step){
	int i;
	int port = -1;
	int rootMacAddress;
	MACQueueElement* head = receiveQueue;
	MACFrame* bestBPDU = NULL;
	broadcastMessage(bridge);
	if (receiveQueue!= NULL)
	bestBPDU = receiveQueue->frame;
	else
		return 0;
	rootMacAddress = receiveQueue->MACaddress;

	port = FindPort(bridge, receiveQueue->MACaddress);

	while(receiveQueue != NULL) {
		if (bridge->portState[FindPort(bridge, receiveQueue->MACaddress)] == Blocked){
			receiveQueue = receiveQueue->next;
			continue;
		}
		if (receiveQueue->frame->SSAP == BRIDGEPROTOCOL && receiveQueue->frame->DSAP == BRIDGEPROTOCOL)	{
			if (CompareBPDU(receiveQueue->frame, bestBPDU) == 1){
				port = FindPort(bridge, receiveQueue->MACaddress);
				rootMacAddress = receiveQueue->MACaddress;
				bestBPDU = receiveQueue->frame;
			}
		}
		else if (receiveQueue->frame->SSAP == IP && receiveQueue->frame->DSAP == IP){
			int sourceAddress = receiveQueue->frame->srcAddress;
			int destinationAddress = receiveQueue->frame->dstAddress;
			int portId = FindPort(bridge, receiveQueue->MACaddress);
			int entry = FindFDBEntry(bridge, sourceAddress);

			if (entry == -1){
				PutFDBEntry(bridge, portId, sourceAddress);
			}
			entry = FindFDBEntry(bridge, destinationAddress);
			if (entry == -1 || destinationAddress == BROADCAST){
				for (i = 0; i < getBridgeNumPorts((bridge)); ++i){
					if (ownPort(portId, i) || isBridgeBlocked(i, bridge))
						continue;
					else
						SendMACFrame(bridge->MACaddresses[i], *receiveQueue->frame);
				}
			}
			else{
				if (portId != entry)
					SendMACFrame(bridge->MACaddresses[entry], *receiveQueue->frame);
			}
		}
		receiveQueue = receiveQueue->next;
	}
	setRootPort(port, rootMacAddress, bestBPDU, bridge);
	setPortsOnBPDU(port, head, bridge);
	return 0;
}


int *GetFDBEntry (Bridge *bridge, int MACaddress)
{ 
	int* retval = malloc(getBridgeNumPorts(bridge) * sizeof(int));
	int i = 0;
	for (i = 0; i < getBridgeNumPorts(bridge); i++){
		retval[i] = 0;
	}

	i = FindFDBEntry(bridge, MACaddress);
	if (i != -1){
		retval[i] = 1;
	}
	return retval;
}


int GetRootPort (Bridge *bridge){
	return bridge->rootMac;
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
	bridge->transmitBPDU->data[3] =  FindPort(bridge, MACaddress);
	return bridge->transmitBPDU;
}


int CompareBPDU(MACFrame* frame1, MACFrame* frame2)
{

	if (l1t(frame1, frame2))
		return 1;
	if (l1f(frame2, frame1))
		return 0;

	if (l2t(frame1, frame2))
		return 1;
	if (l2f(frame2, frame1))
		return 0;

	if (l3t(frame1, frame2))
		return 1;
	if (l3f(frame2, frame1))
		return 0;

	if (l4t(frame1, frame2))
		return 1;
	if (l4f(frame2, frame1))
		return 0;
	return 0;

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

void PutFDBEntry(Bridge* bridge, int port, int MACaddress)
{
	setBridgePort(port, bridge);
		setBridgeStation(MACaddress, bridge);
		increaseBridgeNumFDB(bridge);
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
