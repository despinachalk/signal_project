# Rationale for Subsystem 1 

## Rationale:

The Alert Generation System is designed with a strict adherence to the Single Responsibility Principle
to ensure modularity and high cohesion.

The AlertGenerator acts as the primary orchestrator for this subsystem.
Its only responsibility is to receive incoming telemetry, fetch the corresponding personalized rule
and evaluate the data.
By separating the threshold logic into its own PatientThreshold class,the system is able to easily
handle personalized alert rules
(e.g., Patient A has a different critical heart rate than Patient B)
without bloating the generator's evaluation methods.

When a threshold is breached, the AlertGenerator instantiates an Alert object.
The alert is then passed to the AlertManager.
Separating the generation of the alert from the dispatching of the alert ensures that
if the hospital decides to change how staff are notified
(e.g., pagers vs. tablet pop-ups), the evaluation logic in AlertGenerator remains completely untouched.
The AlertManager handles tracking active alerts, routing them to medical staff and marking them as resolved. 
Fulfilling the domain requirements of a safety-critical hospital environment.


# Rationale for Subsystem 2 

## Rationale:

The Data Storage System is architected with a strong emphasis on the Single Responsibility Principle and domain security.

Isolating distinct behaviors into modular components to maximize maintainability and extensibility.

At the core is the DataStorage interface, which ensures the system remains divided
from the specific implementation details of how data is actually saved
(e.g., local memory vs. an external database).
The concrete implementation SecureDataStorage,arranges the storage of PatientData records,
which include timestamp and versionNumber attributes to track medical history safely over time.

To address the strict privacy requirements of a hospital environment,
security has been abstracted into its own AccessManager class.
Rather than the storage class checking permissions, it passes on that responsibility to the AccessManager,
which evaluates the MedicalStaff object's role and clearance before any data is returned.

Similarly, data lifecycle management is cleanly separated.
A dedicated RetentionPolicy class defines the specific rules for data expiration 
(e.g., keeping ECG data for 30 days, but blood pressure for a year).
The DeletionManager operates independently, evaluating records against these policies
and communicating with the DataStorage interface to remove expired data.
Ensuring compliance without bloating the primary storage logic.

# Rationale for Subsystem 3 

## Rationale:

The Patient Identification System is designed to strictly separate 
the logic of requesting an identity and verifying it.
Also creating a robust boundary between the incoming simulator data and the sensitive hospital records.

The PatientIdentifier serves as the entry point for incoming telemetry.
When data arrives, it asks the IdentityManager to find the corresponding HospitalPatient.
The IdentityManager acts as the central organizer and guardian of this subsystem.
It uses a HospitalDatabase interface to query patient details (like name and medical history).
By using an interface for the database, we ensure the system is loosely grouped
and can easily adapt if the hospital changes its database vendor in the future.

A critical domain requirement for this subsystem is handling edge cases—specifically,
when an incoming simulator ID does not match any existing hospital records.
If the IdentityManager detects an invalid ID, it triggers its handleMismatch method.
Instead of crashing the system or attaching data to the wrong patient, it instantiates an AnomalyRecord.
This ensures the mismatched data is securely logged with a timestamp and resolution status.
Allowing hospital IT or administrative staff to investigate the dropped signals later
without halting the real-time monitoring of valid patients.

# Rationale for Subsystem 4 

## Rationale:

The Data Access Layer is designed using a mix of the Strategy
and Adapter design patterns to ensure maximum flexibility
and separation of concerns.

To handle the different ways data can arrive (TCP, WebSocket, File),
the system uses a shared DataListener interface.
The concrete subclasses (TCPDataListener, etc.) are only responsible for opening a connection 
and reading the raw string data.
They do not know what the data means.

Once a raw string is received, the listener passes it to the DataParser.
The parser's sole responsibility is to translate external formats (like JSON or CSV)
into internal PatientData Java objects.
Finally, the DataSourceAdapter acts as a bridge. 
It takes the standardized PatientData object and routes it to the DataStorage interface from Subsystem 2.
This architecture guarantees that if the hospital decides to switch from WebSockets
to an entirely new protocol in the future, the internal CHMS logic and storage remain completely unaffected.