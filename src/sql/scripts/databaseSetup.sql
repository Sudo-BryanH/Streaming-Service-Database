CREATE TABLE Release(
     ID				INT PRIMARY KEY,
     Name				VARCHAR(50),
     Type				VARCHAR(6),
     ReleaseDate		DATE,
     ArtURL			VARCHAR(150),
     DistributorName	VARCHAR(150) NOT NULL,
     FOREIGN KEY (DistributorName)
         REFERENCES Distributor(Name)
         ON DELETE CASCADE);

CREATE TABLE Song (
    ReleaseID	INT,
    TrackNum	INT,
    Name		VARCHAR(50),
    Duration	INT,
    Genre	CHAR(50),
    Plays	INT,
    PRIMARY KEY (ReleaseID, TrackNum),
    FOREIGN KEY (ReleaseID)
        REFERENCES Release(ID)
        ON DELETE CASCADE);

CREATE TABLE Artist (
    ID				INT PRIMARY KEY,
    Name				VARCHAR(50),
    MonthlyListeners	INT);

CREATE TABLE Distributor(
    Name		VARCHAR(150) PRIMARY KEY,
    Website	VARCHAR(50));

CREATE TABLE UserPayment(
    ID			INT,
    Amount		NUMERIC(10, 2),
    Date			DATE,
    CardNum		INT,
    Username		VARCHAR(50) NOT NULL,
    StreetNum		SMALLINT,
    Street		CHAR(50),
    PostalCode	CHAR(6),
    PRIMARY KEY(ID),
    FOREIGN KEY (CardNum)
        REFERENCES CardTable(CardNum)
        ON DELETE CASCADE,
    FOREIGN KEY (Username)
        REFERENCES PremiumUser(Username)
        ON DELETE CASCADE,
    FOREIGN KEY (StreetNum, Street, PostalCode)
        REFERENCES BillingAddress(StreetNum, Street, PostalCode)
        ON DELETE CASCADE);



CREATE TABLE CompanyPayment
    (ID			INT PRIMARY KEY,
     Amount		NUMERIC(10, 2),
     Date			DATE,
     StreetNum		SMALLINT,
     Street		CHAR(50),
     PostalCode	CHAR(6),
     DistributorName	VARCHAR(150) NOT NULL,
     FOREIGN KEY (DistributorName)
         REFERENCES Distributor(Name)
         ON DELETE CASCADE,
     FOREIGN KEY (StreetNum,Street,PostalCode)
         REFERENCES BillingAddress(StreetNum,Street,PostalCode)
         ON DELETE CASCADE);



CREATE TABLE BillingAddress
    (StreetNum		SMALLINT,
     Street		VARCHAR(50),
     PostalCode	VARCHAR(6),
     PRIMARY KEY (StreetNum, Street, PostalCode)
         FOREIGN KEY (PostalCode)
         REFERENCES PostalCodeCityProvince(PostalCode)
         ON DELETE CASCADE);



CREATE TABLE PostalCodeCityProvince
    (City			VARCHAR(50),
     Province		VARCHAR(2),
     PostalCode 	VARCHAR(6) PRIMARY KEY);



CREATE TABLE User
    (Username		VARCHAR(50) PRIMARY KEY,
     Email		VARCHAR(50) UNIQUE,
     Password		VARCHAR(50),
     CreationDate	DATE);



CREATE TABLE FreeUser
    (Username		VARCHAR(50) PRIMARY KEY,
     AdsServed		INT,
     FOREIGN KEY (Username)
         REFERENCES User(Username)
         ON DELETE CASCADE);





CREATE TABLE PremiumUser
    (Username		VARCHAR(50) PRIMARY KEY,
     SubStartDate	DATE,
     SubEndDate	DATE,
     FOREIGN KEY (Username)
         REFERENCES User(Username)
         ON DELETE CASCADE);



CREATE TABLE Playlist
    (Username	VARCHAR(50),
     Name 	VARCHAR(50),
     PRIMARY KEY (Username, Name),
     FOREIGN KEY (Username)
         REFERENCES User(Username)
         ON DELETE CASCADE);



CREATE TABLE PlaylistIsIn
    (Username	VARCHAR(50),
     Name		VARCHAR(50),
     ReleaseID INT,
     TrackNum	INT,
     PRIMARY KEY (Username, Name, TrackNum, ReleaseID),
     FOREIGN KEY (Username, Name)
         REFERENCES Playlist(Username, Name)
         ON DELETE CASCADE,
     FOREIGN KEY (TrackNum, ReleaseID)
         REFERENCES Song(TrackNum, ReleaseID)
         ON DELETE CASCADE);



CREATE TABLE AddsToLibrary
    (ReleaseID	INT,
     TrackNum	INT,
     Username	VARCHAR(50),
     DOWNLOADED NUMERIC(1,0),
     LIKED NUMERIC(1,0),
     PRIMARY KEY (ReleaseID, TrackNum, Username),
     FOREIGN KEY (Username)
         REFERENCES Song (ReleaseID, TrackNum)
         ON DELETE CASCADE,
     FOREIGN KEY (Username)
         REFERENCES User(Name)
         ON DELETE CASCADE);



CREATE TABLE FeaturedIn
    (ArtistID	INT,
     ReleaseID	INT,
     TrackNum	INT,
     PRIMARY KEY (ArtistID, ReleaseID, TrackNum),
     FOREIGN KEY (ArtistID)
         REFERENCES Artist(ID)
         ON DELETE CASCADE,
     FOREIGN KEY (ReleaseID, TrackNum)
         REFERENCES Song(ReleaseID, TrackNum)
         ON DELETE CASCADE);



CREATE TABLE Follows(
    Username	VARCHAR(50),
    ArtistID	INT,
    PRIMARY KEY (Username, ArtistID),
    FOREIGN KEY (Username)
        REFERENCES User(Username)
        ON DELETE CASCADE,
    FOREIGN KEY (ArtistID)
        REFERENCES Artist(ID)
        ON DELETE CASCADE);



CREATE TABLE Creates(
    ReleaseID	INT,
    ArtistID	INT,
    PRIMARY KEY (ReleaseID, ArtistID),
    FOREIGN KEY (Username)
        REFERENCES Artist (ID)
        ON DELETE CASCADE,
    FOREIGN KEY (ReleaseID)
        REFERENCES Release(ID)
        ON DELETE CASCADE);



INSERT INTO Release VALUES

INSERT INTO Release VALUES (1, 'Whenever You Need Somebody', 'Album', 1987-11-12, 	 'https://cdn.spotube.com/img/rickroll.png', 'BMG Rights Management (UK) Limited');

INSERT INTO Release VALUES (2, 'Astro Lounge', 'Album', 1999-06-08, 'https://cdn.spotube.com/img/shrek.png', 'Interscope Geffen (A&M) Records');

INSERT INTO Release VALUES (3, 'Curb Your Enthusiasm (Music From the TV Series)', 'Album', 2006-04-17, 'https://cdn.spotube.com/img/frolic.png', 'Mellowdrama Records');

INSERT INTO Release VALUES (4, 'Tritio Jatra', 'Album', 2006-04-17, 'https://cdn.spotube.com/img/nemesis.png', 'Studio Bangi');

INSERT INTO Release VALUES(5, 'Superunknown', 'Album', 2006-04-17, 'https://cdn.spotube.com/img/soundgarden.png', 'Sony Music');



INSERT INTO Song VALUES

INSERT INTO Song VALUES(1, 1, 'Never Gonna Give You Up', 214, 'Pop', 213422997);

INSERT INTO Song VALUES(1, 2, 'Whenever You Need Somebody', 243, 'Pop', 1842825);

INSERT INTO Song VALUES (2, 4, 'All Star', 190, 'Alternative', 692894289);

INSERT INTO Song VALUES(3, 1, 'Frolic', 210, 'Soundtrack', 5359039);

INSERT INTO Song VALUES (4, 4, 'Nirbashon', 210, 'Rock', 5359039);





INSERT INTO Artist VALUES (1, 'Rick Astley', 5829256);

INSERT INTO Artist VALUES  (2, 'Smash Mouth', 3950387);

INSERT INTO Artist VALUES  (3, 'Curb Your Enthusiasm', 38358);
INSERT INTO Artist VALUES (4, 'Luciano Michelini', 35294);

INSERT INTO Artist VALUES (5, 'Nemesis', 200000);



INSERT INTO Distributor VALUES

INSERT INTO Distributor VALUES ('BMG Rights Management (UK) Limited', 'https://www.bmg.com/');

INSERT INTO Distributor VALUES ('Interscope Geffen (A&M) Records', 'https://www.interscope.com');

INSERT INTO Distributor VALUES ('Mellowdrama Records', NULL);

INSERT INTO Distributor VALUES ('Studio Bangi', ' https://www.studio-bangi.com');

INSERT INTO Distributor VALUES ('Sony Music', 'https://www.sonymusic.com');



INSERT INTO UserPayment VALUES

INSERT INTO UserPayment VALUES (1, 7, 2022-03-01, 2394235323332438, 'bryanh', 3235, 'Joyce st.', 'V6E7F3');

INSERT INTO UserPayment VALUES (2, 11, 2022-03-01, 5434235323332438, 'sleepy', 1234, 'Joyce st.', 'V6E7F3');

INSERT INTO UserPayment VALUES (3, 12, 2022-02-01, 1234353233324332, 'A113', 1234, 'Cambie st.', 'V6E7F3');

INSERT INTO UserPayment VALUES (4, 15, 2022-02-01, 7893353233324342, 'notstevejobs', 1234, 'Cambie st.', 'V4E7F3');

INSERT INTO UserPayment VALUES (5, 15, 2022-02-01, 8989353233324023, 'celloist', 7843, 'Cambie st.', 'V6E7F3');





INSERT INTO CardTable VALUES ('Visa', 2394235323332438, 2025-01-01);

INSERT INTO CardTable VALUES ('Amex', 5434235323332438, 2026-01-01);

INSERT INTO CardTable VALUES ('Mastercard', 1234353233324332, 2027-01-01);

INSERT INTO CardTable VALUES ('Visa', 7893353233324342, 2025-02-01);

INSERT INTO CardTable VALUES ('Amex', 8989353233324023, 2023-07-01);





INSERT INTO CompanyPayment VALUES (1, 7000, 2022-03-01, 3235, 'Courtney St.', 'V6E5E3', 'Sony Music');

INSERT INTO CompanyPayment VALUES (2, 1100, 2022-03-01, 1234, 'Camosun St.', 'V6E789', 'Studio Bangi');

INSERT INTO CompanyPayment VALUES (3, 1200, 2022-02-01, 1234, 'Cambie st.', 'V8E7F3', 'BMG Rights Management (UK) Limited');

INSERT INTO CompanyPayment VALUES (4, 1500, 2022-02-01, 1234, 'Hastings st.', 'V6E7K3', 'Interscope Geffen (A&M) Records');

INSERT INTO CompanyPayment VALUES (5, 1500, 2022-02-01, 7843, 'Manitoba st.', 'V6E1K2', 'Mellowdrama Records');





INSERT INTO BillingAddress VALUES (3235, 'Joyce st.', 'V6E7F3');

INSERT INTO BillingAddress VALUES (1234, 'Joyce st.', 'V6E7F3');
INSERT INTO BillingAddress VALUES (1234, 'Cambie st.', 'V4E7F3');

INSERT INTO BillingAddress VALUES (2829, 'Cambie st.', 'V6E7F3');

INSERT INTO BillingAddress VALUES (7843, 'Cambie st.', 'V6E7F3');

INSERT INTO BillingAddress VALUES (3235, 'Courtney St.', 'V6E5E3');

INSERT INTO BillingAddress VALUES (1234, 'Camosun St.', 'V6E789');

INSERT INTO BillingAddress VALUES (2948, 'Cambie st.', 'V4E7F3');

INSERT INTO BillingAddress VALUES (1234, 'Hastings st.', 'V8E7K3',);

INSERT INTO BillingAddress VALUES (7843, 'Manitoba st.', 'V6E1K2');

INSERT INTO BillingAddress VALUES (2345, 'Kenneth st.', 'G5E5C6');

INSERT INTO BillingAddress VALUES (8343, 'Montgomery st.', 'H4R7J8');

INSERT INTO BillingAddress VALUES (8583, 'Elliot st.', 'K9L0H1')



INSERT INTO PostalCodeCityProvince VALUES ('Vancouver', 'BC', 'V6E7F3');

INSERT INTO PostalCodeCityProvince VALUES ('Vancouver', 'BC', 'V4E7F3');
INSERT INTO PostalCodeCityProvince VALUES ('Vancouver', 'BC,' 'V6E5E3');
INSERT INTO PostalCodeCityProvince VALUES ('Vancouver', 'BC', 'V6E789');
INSERT INTO PostalCodeCityProvince VALUES ('Vancouver', 'BC', 'V8E7K3');

INSERT INTO PostalCodeCityProvince VALUES ('Vancouver', 'BC', 'V6E7K3');

INSERT INTO PostalCodeCityProvince VALUES ('Vancouver', 'BC', 'V6E1K2');

INSERT INTO PostalCodeCityProvince VALUES ('Calgary', 'AB', 'G5E5C6');

INSERT INTO PostalCodeCityProvince VALUES ('Winnepeg', 'MB', 'H4R7J8');

INSERT INTO PostalCodeCityProvince VALUES ('Toronto', 'ON', 'K9L0H1');



INSERT INTO User VALUES ('eenie meenie', 'eenie@gmail.com', 'whatever', 2021-02-09);

INSERT INTO User VALUES ('sleepy', 'sleepingInClass@gmail.com', 'allnighter', 2023-01-01);

INSERT INTO User VALUES ('celloist', 'cello-ing@yahoo.com', 'prez', 2021-04-06);

INSERT INTO User VALUES ('notstevejobs', 'jobs@gmail.com', 'googlebest', 2020-01-09);

INSERT INTO User VALUES ('jfk1975', 'jfk@yahoo.com', 'ishouldbedead', 2019-01-03);

INSERT INTO User VALUES ('bryanh', 'bryan@gmail.com', 'CS213Sucks', 2022 - 01 - 01);

INSERT INTO User VALUES ('harperk', 'harper@gmail.com', 'CS310Sucks', 2022 - 02 - 02);

INSERT INTO User VALUES ('dhrubok', 'dhrubo@hotmail.com', 'CS210Sucks', 2021 - 09 - 01);

INSERT INTO User VALUES ('kimdol', 'kimdol@yahoo.com', 'CS304Sucks', 2022 - 03 - 04);

INSERT INTO User VALUES ('A113, 'A113@outlook.com', 'CS304Sucks', 2022-09-10);






INSERT INTO FreeUser VALUES ('harperk', 13);

INSERT INTO FreeUser VALUES ('dhrubok', 16);

INSERT INTO FreeUser VALUES ('kimdol', 0);

INSERT INTO FreeUser VALUES ('jfk1975', 13);

INSERT INTO FreeUser VALUES ('eenie meenie', 6);




INSERT INTO PremiumUser VALUES ('bryanh', 2022-03-01, 2023-03-01);

INSERT INTO PremiumUser VALUES ('A113', 2022-02-01, 2023-02-01);

INSERT INTO PremiumUser VALUES ('sleepy', 2023-03-01, 2024-02-01);

INSERT INTO PremiumUser VALUES ('celloist', 2023-03-01, 2024-02-01);

INSERT INTO PremiumUser VALUES ('notstevejobs', 2021-03-01, 2022-02-01);





INSERT INTO Playlist VALUES ('bryanh','lofi');

INSERT INTO Playlist VALUES ('eenie meenie','90s jam',);

INSERT INTO Playlist VALUES ('kimdol','study-session');

INSERT INTO Playlist VALUES ('jfk1975','80s glam metal');
INSERT INTO Playlist VALUES ('A113','alt jam');





INSERT INTO PlaylistIsIn VALUES ('bryanh','lofi',1,1);

INSERT INTO PlaylistIsIn VALUES ('bryanh','lofi',1,2);

INSERT INTO PlaylistIsIn VALUES ('kimdol','study-session',2,4);

INSERT INTO PlaylistIsIn VALUES ('eenie meenie',' 90s jam',4,4);

INSERT INTO PlaylistIsIn VALUES ('jfk1975','80s glam metal',3,1);





INSERT INTO AddsToLibrary VALUES (1,1,'bryanh',0,1);

INSERT INTO AddsToLibrary VALUES (1,2,'bryanh',0,0);

INSERT INTO AddsToLibrary VALUES (2,4,'kimdol',0,1);

INSERT INTO AddsToLibrary VALUES (4,4,'sleepy',1,0);
INSERT INTO AddsToLibrary VALUES (3,1,'A113',1,1);





INSERT INTO FeaturedIn VALUES (1,1,1);

INSERT INTO FeaturedIn VALUES (2,1,2);

INSERT INTO FeaturedIn VALUES (3,2,4);

INSERT INTO FeaturedIn VALUES (4,4,4);

INSERT INTO FeaturedIn VALUES (5,3,1);




INSERT INTO Follows VALUES ('bryanh',1);

INSERT INTO Follows VALUES ('kimdol',2);

INSERT INTO Follows VALUES ('eenie meenie',3);

INSERT INTO Follows VALUES ('A113','notstevejobs',2);

INSERT INTO Follows VALUES ('sleepy',5);



INSERT INTO Creates VALUES (1,1);

INSERT INTO Creates VALUES (2,2);

INSERT INTO Creates VALUES (3,3);

INSERT INTO Creates VALUES(4,4);

INSERT INTO Creates VALUES (5,5);

