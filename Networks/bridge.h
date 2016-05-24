/* 
 * file: bridge.h
 * ==============
 *
 * The bridge.h file defines the interface for a set of functions that 
 * simulates the operation of the bridge according to the spanning tree
 * algorithm   
 * 
 */

#ifndef _bridge_h
#define _bridge_h

#define MAXNUMBEROFPORTSINABRIDGE 1024 /* Max number of ports in a bridge */
#define MAXNUMBEROFFDBENTRIES 1024     /* Max number of FDB entries */

#define ALLBRIDGES   -100   /* multicast/broadcast addresses are negative */
#define BROADCAST -1        /* will be used as destination address in */
                            /* broadcast frames */
#define BRIDGEPROTOCOL 66   /* to be used for DSAP, SSAP values in BPDUs */
#define IP           1      /* will be used as DSAP, SSAP in data packets
			       sent by stations */
#define FIELDS 250



typedef enum {Forwarding, Blocking} PortStateType;
typedef enum {Root, Blocked, Designated} PortState;


typedef struct
{
  int dstAddress;		/* MAC address of destination */
  int srcAddress;		/* MAC addess of source */
  int length;			/* length of frame in bytes,
				   for BPDUs should be set to 35 */
  int DSAP;
  int SSAP;			/* protocol number, set DSAP and SSAP
				   to the BRIDGEPROTOCOL value in
				   BPDUs */
  int data[FIELDS];		/* This array contains the data in the
				   frame. You are free to organize the
				   BPDU data in this array the way you
				   want, except as noted for the
				   GetConfigBPDUatPort() function
				   below */
} MACFrame;


typedef struct MACQueueElement
{
  MACFrame *frame;                  /* pointer to the frame */ 
  struct MACQueueElement *next;    /* pointer to the next
				       MACQueueElement */
  int MACaddress;                   /* identifies port where frame is
				       received */
                                    /* MACaddress refers to the
				       globalPortID */
} MACQueueElement;



typedef struct
{
  int station;
  int port;
} FDBEntry;

typedef struct
{
  int rootId;
  int rootMac;
  int bridgeID;  /* bridge ID */
  int numPorts;  /* number or ports in the bridge */
  /* Add more elements here as needed.... */
  int* MACaddresses;   // Element 0 is the mac of port 0 
  PortState* portState; // User defined port state
  MACFrame* transmitBPDU; // MACFrame that contains the BPDU that this bridge would transmit
  FDBEntry FDB[MAXNUMBEROFFDBENTRIES];
  int numFDB;

} Bridge;

void *AllocateBridge();


void InitializeBridge(Bridge *bridge, int bridgeID, int numPorts, int
		      *MACaddresses, int ignore1, int ignore2, int ignore3);

int Execute(Bridge *bridge, MACQueueElement *receiveQueue, unsigned long step);


int *GetFDBEntry (Bridge *bridge, int MACaddress);


PortStateType GetPortState (Bridge *bridge, int MACaddress);


int GetRootPort (Bridge *bridge);


MACFrame *GetConfigBPDUatPort (Bridge *bridge, int MACaddress);


void BridgeCleanup(Bridge* bridge);


int FindPort(Bridge* bridge, int MACaddress);


int CompareBPDU(MACFrame* frame1, MACFrame* frame2);


int FindFDBEntry(Bridge* bridge, int MACaddress);

void PutFDBEntry(Bridge* bridge, int port, int MACaddress);

#endif

