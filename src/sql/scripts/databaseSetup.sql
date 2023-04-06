CREATE TABLE Distributor
(
    Name    VARCHAR(150) PRIMARY KEY,
    Website VARCHAR(50)
);

CREATE TABLE Users
(
    Username     VARCHAR(50) PRIMARY KEY,
    Email        VARCHAR(50) UNIQUE,
    Password     VARCHAR(100),
    CreationDate DATE
);

CREATE TABLE FreeUser
(
    Username  VARCHAR(50) PRIMARY KEY,
    AdsServed INT,
    FOREIGN KEY (Username)
        REFERENCES Users (Username)
            ON DELETE CASCADE
);

CREATE TABLE PremiumUser
(
    Username     VARCHAR(50) PRIMARY KEY,
    SubStartDate DATE,
    SubEndDate   DATE,
    FOREIGN KEY (Username)
        REFERENCES Users (Username)
            ON DELETE CASCADE
);

CREATE TABLE Releases
(
    ID              INT PRIMARY KEY,
    Name            VARCHAR(50),
    Type            VARCHAR(6),
    ReleaseDate     DATE,
    ArtURL          VARCHAR(150),
    DistributorName VARCHAR(150) NOT NULL,
    FOREIGN KEY (DistributorName)
        REFERENCES Distributor (Name)
            ON DELETE CASCADE
);

CREATE TABLE Song
(
    ReleaseID INT,
    TrackNum  INT,
    Name      VARCHAR(50),
    Duration  INT,
    Genre     VARCHAR(50),
    Plays     INT,
    PRIMARY KEY (ReleaseID, TrackNum),
    FOREIGN KEY (ReleaseID)
        REFERENCES Releases (ID)
            ON DELETE CASCADE
);

CREATE TABLE Artist
(
    ID               INT PRIMARY KEY,
    Name             VARCHAR(50)
);

CREATE TABLE CardTable
(
    CardCompany CHAR(50),
    CardNum     INT PRIMARY KEY,
    CardExpiry  DATE
);

CREATE TABLE PostalCodeCityProvince
(
    City       VARCHAR(50),
    Province   VARCHAR(2),
    PostalCode VARCHAR(6) PRIMARY KEY
);

CREATE TABLE BillingAddress
(
    StreetNum  SMALLINT,
    Street     VARCHAR(50),
    PostalCode VARCHAR(6),
    PRIMARY KEY (StreetNum, Street, PostalCode),
    FOREIGN KEY (PostalCode)
        REFERENCES PostalCodeCityProvince (PostalCode)
            ON DELETE CASCADE
);

CREATE TABLE UserPayment
(
    ID         INT,
    Amount     NUMERIC(10, 2),
    "Date"       DATE,
    CardNum    INT,
    Username   VARCHAR(50) NOT NULL,
    StreetNum  SMALLINT,
    Street     VARCHAR(50),
    PostalCode VARCHAR(6),
    PRIMARY KEY (ID),
    FOREIGN KEY (CardNum)
        REFERENCES CardTable (CardNum)
            ON DELETE CASCADE,
    FOREIGN KEY (Username)
        REFERENCES PremiumUser (Username)
            ON DELETE CASCADE,
    FOREIGN KEY (StreetNum, Street, PostalCode)
        REFERENCES BillingAddress (StreetNum, Street, PostalCode)
            ON DELETE CASCADE
);

CREATE TABLE CompanyPayment
(
    ID              INT PRIMARY KEY,
    Amount          NUMERIC(10, 2),
    "Date"            DATE,
    StreetNum       SMALLINT,
    Street          VARCHAR(50),
    PostalCode      VARCHAR(6),
    DistributorName VARCHAR(150) NOT NULL,
    FOREIGN KEY (DistributorName)
        REFERENCES Distributor (Name)
            ON DELETE CASCADE,
    FOREIGN KEY (StreetNum, Street, PostalCode)
        REFERENCES BillingAddress (StreetNum, Street, PostalCode)
            ON DELETE CASCADE
);

CREATE TABLE Playlist
(
    Username VARCHAR(50),
    Name     VARCHAR(50),
    PRIMARY KEY (Username, Name),
    FOREIGN KEY (Username)
        REFERENCES Users (Username)
            ON DELETE CASCADE
);

CREATE TABLE PlaylistIsIn
(
    Username  VARCHAR(50),
    Name      VARCHAR(50),
    ReleaseID INT,
    TrackNum  INT,
    PRIMARY KEY (Username, Name, TrackNum, ReleaseID),
    FOREIGN KEY (Username, Name)
        REFERENCES Playlist (Username, Name)
            ON DELETE CASCADE,
    FOREIGN KEY (TrackNum, ReleaseID)
        REFERENCES Song (TrackNum, ReleaseID)
            ON DELETE CASCADE
);

CREATE TABLE AddsToLibrary
(
    ReleaseID  INT,
    TrackNum   INT,
    Username   VARCHAR(50),
    Downloaded NUMERIC(1, 0),
    Liked      NUMERIC(1, 0),
    PRIMARY KEY (ReleaseID, TrackNum, Username),
    FOREIGN KEY (ReleaseID,TrackNum)
        REFERENCES Song (ReleaseID, TrackNum)
            ON DELETE CASCADE,
    FOREIGN KEY (Username)
        REFERENCES Users (Username)
            ON DELETE CASCADE
);

CREATE TABLE FeaturedIn
(
    ArtistID  INT,
    ReleaseID INT,
    TrackNum  INT,
    PRIMARY KEY (ArtistID, ReleaseID, TrackNum),
    FOREIGN KEY (ArtistID)
        REFERENCES Artist (ID)
            ON DELETE CASCADE,
    FOREIGN KEY (ReleaseID, TrackNum)
        REFERENCES Song (ReleaseID, TrackNum)
            ON DELETE CASCADE
);

CREATE TABLE Creates
(
    ReleaseID INT,
    ArtistID  INT,
    PRIMARY KEY (ReleaseID, ArtistID),
    FOREIGN KEY (ArtistID)
        REFERENCES Artist (ID)
            ON DELETE CASCADE,
    FOREIGN KEY (ReleaseID)
        REFERENCES Releases (ID)
            ON DELETE CASCADE
);

CREATE VIEW CountSongsPerArtist
AS SELECT ID, Name, COUNT(*) as Count
    FROM ((SELECT a1.ID as ID, a1.Name, c1.ReleaseID as RID, s1.TrackNum FROM Artist a1, Song s1, Creates c1
            WHERE a1.ID = c1.ArtistID AND c1.ReleaseID = s1.ReleaseID)
    UNION (SELECT a2.ID as ID, a2.Name, s2.ReleaseID as RID, s2.TrackNum FROM Artist a2, Song s2, FeaturedIn f2
            WHERE a2.ID = f2.ArtistID AND s2.ReleaseID = f2.ReleaseID AND s2.TrackNum = f2.TrackNum))
   GROUP BY ID, Name;


INSERT INTO Distributor
VALUES ('BMG Rights Management (UK) Limited', 'https://www.bmg.com/');
INSERT INTO Distributor
VALUES ('Interscope Geffen (A&M) Records', 'https://www.interscope.com');
INSERT INTO Distributor
VALUES ('Mellowdrama Records', NULL);
INSERT INTO Distributor
VALUES ('Studio Bangi', ' https://www.studio-bangi.com');
INSERT INTO Distributor
VALUES ('Sony Music', 'https://www.sonymusic.com');
INSERT INTO Distributor
VALUES ('Swinglong Ltd.', NULL);
INSERT INTO Distributor
VALUES ('Monstercat', 'https://www.monstercat.com/');
INSERT INTO Distributor
VALUES ('Warner Music Group', 'https://www.wmg.com/');

INSERT INTO Users
VALUES ('eenie meenie', 'eenie@gmail.com', '85738f8f9a7f1b04b5329c590ebcb9e425925c6d0984089c43a022de4f19c281', '2021-02-09');
INSERT INTO Users
VALUES ('sleepy', 'sleepingInClass@gmail.com', '1510a248a4c1b04080c784b146b67252c6fcfa790a28aa0cef1b86a67a884126', '2023-01-01');
INSERT INTO Users
VALUES ('celloist', 'cello-ing@yahoo.com', '0d303c1ac4b39b31576f09333e181e5e27b4167aeafd5e3bce3c871bb8629f56', '2021-04-06');
INSERT INTO Users
VALUES ('notstevejobs', 'jobs@gmail.com', '2d665312f36c582fd82a5fc80e82ea3cb5b77b2b250e7d8e54830374bbdcccf9', '2020-01-09');
INSERT INTO Users
VALUES ('jfk1975', 'jfk@yahoo.com', 'aa98ae2f5bc668bd7e3795732a89ad3722cf3868eed7a3fc0e671447aa7c5a6c', '2019-01-03');
INSERT INTO Users
VALUES ('bryanh', 'bryan@gmail.com', 'bcb3f69829ca414398514d9ec8a4c2214023c77a79e49f6eb1e4869a90741309', '2022-01-01');
INSERT INTO Users
VALUES ('harperk', 'harper@gmail.com', '9d7eb6416f0f999a65a35fdecbba884be223a41417d1d6ba6f60afb78a6056bb', '2022-02-02');
INSERT INTO Users
VALUES ('dhrubok', 'dhrubo@hotmail.com', 'a087b25fa26d62555cfa02310c095b86e065cfcdc8fadeacbfcf4252e9c652e3', '2021-09-01');
INSERT INTO Users
VALUES ('kimdol', 'kimdol@yahoo.com', '4cfd8250b504a3d141f853397a90bcfda8c7a291549d7ab37448fe59ee97c435', '2022-03-04');
INSERT INTO Users
VALUES ('A113', 'A113@outlook.com', '4cfd8250b504a3d141f853397a90bcfda8c7a291549d7ab37448fe59ee97c435', '2022-09-10');
INSERT INTO Users
VALUES ('admin', 'admin@admin.com', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', '2023-04-01');

INSERT INTO FreeUser
VALUES ('harperk', 13);
INSERT INTO FreeUser
VALUES ('dhrubok', 16);
INSERT INTO FreeUser
VALUES ('kimdol', 0);
INSERT INTO FreeUser
VALUES ('jfk1975', 13);
INSERT INTO FreeUser
VALUES ('eenie meenie', 6);

INSERT INTO PremiumUser
VALUES ('bryanh', '2022-03-01', '2023-03-01');
INSERT INTO PremiumUser
VALUES ('A113', '2022-02-01', '2023-02-01');
INSERT INTO PremiumUser
VALUES ('sleepy', '2023-03-01', '2024-02-01');
INSERT INTO PremiumUser
VALUES ('celloist', '2023-03-01', '2024-02-01');
INSERT INTO PremiumUser
VALUES ('notstevejobs', '2021-03-01', '2022-02-01');
INSERT INTO PremiumUser
VALUES ('admin', '2023-04-01', '2024-04-01');

INSERT INTO Releases
VALUES (1, 'Whenever You Need Somebody', 'Album', '1987-11-12', 'https://cdn.spotube.com/img/rickroll.png',
        'BMG Rights Management (UK) Limited');
INSERT INTO Releases
VALUES (2, 'Astro Lounge', 'Album', '1999-06-08', 'https://cdn.spotube.com/img/shrek.png',
        'Interscope Geffen (A&M) Records');
INSERT INTO Releases
VALUES (3, 'Curb Your Enthusiasm (Music From the TV Series)', 'Album', '2006-04-17',
        'https://cdn.spotube.com/img/frolic.png', 'Mellowdrama Records');
INSERT INTO Releases
VALUES (4, 'Tritio Jatra', 'Album', '2006-04-17', 'https://cdn.spotube.com/img/nemesis.png', 'Studio Bangi');
INSERT INTO Releases
VALUES (5, 'Superunknown', 'EP', '2006-04-17', 'https://cdn.spotube.com/img/soundgarden.png', 'Sony Music');
INSERT INTO Releases
VALUES (6, 'Take On Me (2017 Acoustic)', 'Single', '1984-09- 01', 'https://cdn.spotube.com/img/aha.png',
        'Swinglong Ltd.');
INSERT INTO Releases
VALUES (7, 'Meowfest', 'Album', '2003-10-10', 'https://cdn.spotube.com/img/kitty.png',
        'Monstercat');
INSERT INTO Releases
VALUES (8, 'The Barker is in Town', 'EP', '2003-01-01', 'https://cdn.spotube.com/img/doggo.png',
        'Monstercat');
INSERT INTO Releases
VALUES (9, 'Dogs Don''t Suck', 'Single', '2003-10-10', 'https://cdn.spotube.com/img/friends.png',
        'Monstercat');
INSERT INTO Releases
VALUES (10, 'Only Me', 'EP', '2015-02-27', 'https://cdn.spotube.com/img/notuu.png',
        'Warner Music Group');
INSERT INTO Releases
VALUES (11, 'Sonder', 'EP', '2015-08-12', 'https://cdn.spotube.com/img/sonder.png',
        'Warner Music Group');
INSERT INTO Releases
VALUES (12, 'Not A Chance', 'Single', '2012-05-29', 'https://cdn.spotube.com/img/nac.png',
        'Monstercat');
INSERT INTO Releases
VALUES (13, 'Zeka', 'Single', '2018-12-31', 'https://cdn.spotube.com/img/zeka.png',
        'Sony Music');
INSERT INTO Releases
VALUES (14, 'Basic', 'Single', '2018-12-31', 'https://cdn.spotube.com/img/basic.png',
        'Sony Music');

INSERT INTO Artist
VALUES (1, 'Rick Astley');
INSERT INTO Artist
VALUES (2, 'Smash Mouth');
INSERT INTO Artist
VALUES (3, 'Curb Your Enthusiasm');
INSERT INTO Artist
VALUES (4, 'Luciano Michelini');
INSERT INTO Artist
VALUES (5, 'Nemesis');
INSERT INTO Artist
VALUES (6, 'Soundgarden');
INSERT INTO Artist
VALUES (7, 'a-ha');
INSERT INTO Artist
VALUES (8, 'DJ KitKat');
INSERT INTO Artist
VALUES (9, 'Mr. Barker');
INSERT INTO Artist
VALUES (10, 'Not UU');
INSERT INTO Artist
VALUES (11, 'Zeka');
INSERT INTO Artist
VALUES (12, 'John Doe');

INSERT INTO Creates
VALUES (1, 1);
INSERT INTO Creates
VALUES (2, 2);
INSERT INTO Creates
VALUES (3, 3);
INSERT INTO Creates
VALUES (3, 4);
INSERT INTO Creates
VALUES (4, 5);
INSERT INTO Creates
VALUES (5, 6);
INSERT INTO Creates
VALUES (6, 7);
INSERT INTO Creates
VALUES (7, 8);
INSERT INTO Creates
VALUES (8, 9);
INSERT INTO Creates
VALUES (9, 8);
INSERT INTO Creates
VALUES (9, 9);
INSERT INTO Creates
VALUES (10, 10);
INSERT INTO Creates
VALUES (11, 10);
INSERT INTO Creates
VALUES (12, 8);
INSERT INTO Creates
VALUES (13, 11);
INSERT INTO Creates
VALUES (14, 12);

INSERT INTO Song
VALUES (1, 1, 'Never Gonna Give You Up', 214, 'Pop', 213422997);
INSERT INTO Song
VALUES (1, 2, 'Whenever You Need Somebody', 244, 'Pop', 1842825);
INSERT INTO Song
VALUES (1, 3, 'Together Forever', 206, 'Pop', 1504161);
INSERT INTO Song
VALUES (1, 4, 'It Would Take a Strong Strong Man', 221, 'Pop', 805086);
INSERT INTO Song
VALUES (1, 5, 'The Love Has Gone', 260, 'Pop', 316040);
INSERT INTO Song
VALUES (1, 6, 'Don''t Say Goodbye', 249, 'Pop', 84133);
INSERT INTO Song
VALUES (1, 7, 'Slipping Away', 242, 'Pop', 544835);
INSERT INTO Song
VALUES (1, 8, 'No More Looking for Love', 243, 'Pop', 415202);
INSERT INTO Song
VALUES (1, 9, 'You Move Me', 243, 'Pop', 949624);
INSERT INTO Song
VALUES (1, 10, 'When I Fall in Love', 183, 'Pop', 595731);
INSERT INTO Song
VALUES (2, 1, 'Who''s There', 214, 'Alternative', 150983);
INSERT INTO Song
VALUES (2, 2, 'Diggin'' Your Scene', 189, 'Alternative', 3448250);
INSERT INTO Song
VALUES (2, 3, 'I Just Wanna See',183 , 'Alternative', 5094786);
INSERT INTO Song
VALUES (2, 4, 'Waste', 205, 'Alternative', 8007989);
INSERT INTO Song
VALUES (2, 5, 'All Star', 190, 'Alternative', 692894289);
INSERT INTO Song
VALUES (2, 6, 'Satellite', 218, 'Alternative', 5261034);
INSERT INTO Song
VALUES (3, 1, 'Frolic', 210, 'Soundtrack', 5359039);
INSERT INTO Song
VALUES (3, 2, 'Bubba Dub Bossa', 108, 'Soundtrack', 92825);
INSERT INTO Song
VALUES (3, 3, 'Beach Parade', 100, 'Soundtrack', 91884);
INSERT INTO Song
VALUES (3, 4, 'For Whom the Bell Tolls', 64, 'Soundtrack', 93682);
INSERT INTO Song
VALUES (3, 5, 'The Stranger', 142, 'Soundtrack', 77515);
INSERT INTO Song
VALUES (4, 1, 'Kobe', 254, 'Rock', 249567);
INSERT INTO Song
VALUES (4, 2, 'Bir', 401, 'Rock', 506029);
INSERT INTO Song
VALUES (4, 3, 'Tritio Jatra', 254, 'Rock', 645791);
INSERT INTO Song
VALUES (4, 4, 'Nirbashon', 234, 'Rock', 5359039);
INSERT INTO Song
VALUES (5, 1, 'Let Me Down', 313, 'Hard Rock', 508989);
INSERT INTO Song
VALUES (5, 2, 'My Wave', 304, 'Hard Rock', 623314);
INSERT INTO Song
VALUES (5, 3, 'Fell On Black Days', 396, 'Hard Rock', 726962);
INSERT INTO Song
VALUES (6, 1, 'Take On Me (2017 Acoustic)', 184, 'Pop', 6666852);
INSERT INTO Song
VALUES (7, 1, 'Start Hop, Jump', 294, 'Dance', 1412892);
INSERT INTO Song
VALUES (7, 2, 'Pounce Bounce House', 287, 'Dance', 3295024);
INSERT INTO Song
VALUES (7, 3, 'Pawsitivity', 124, 'Pop', 12895982);
INSERT INTO Song
VALUES (7, 4, 'Tails Up, Tails Down', 252, 'Dance', 2932104);
INSERT INTO Song
VALUES (7, 5, 'Meowrr', 244, 'Alternative', 292501);
INSERT INTO Song
VALUES (7, 6, 'Just a Mere Kitten', 355, 'Ballad', 214214901);
INSERT INTO Song
VALUES (7, 7, 'Show Your Stripes', 294, 'Dance', 125292);
INSERT INTO Song
VALUES (7, 8, 'Not 4 You', 204, 'Dance', 25920);
INSERT INTO Song
VALUES (7, 9, 'Kickback, Relax', 292, 'Dance', 242425);
INSERT INTO Song
VALUES (7, 10, 'Meowrr Part 2', 189, 'Dance', 1412892);
INSERT INTO Song
VALUES (7, 11, 'High on Catnip St.', 193, 'Hip-hop', 12591250);
INSERT INTO Song
VALUES (7, 12, 'KitKat', 290, 'Dance', 215912590);
INSERT INTO Song
VALUES (7, 13, 'Unlucky', 182, 'Dance', 12590125);
INSERT INTO Song
VALUES (7, 14, 'Dogs Suck', 450, 'Pop', 12590125);
INSERT INTO Song
VALUES (7, 15, 'Paws Off My Ending', 900, 'Trance', 2592050);
INSERT INTO Song
VALUES (8, 1, 'Woof, Poof Roof', 241, 'Electronic', 1412892);
INSERT INTO Song
VALUES (8, 2, 'The Barker is in Town', 192, 'Hip-hop', 3295024);
INSERT INTO Song
VALUES (8, 3, 'Barked Up the Wrong Tree', 285, 'Electronic', 12895982);
INSERT INTO Song
VALUES (8, 4, 'Cone of Shame', 301, 'Classical', 2932104);
INSERT INTO Song
VALUES (8, 5, 'Wag, Wag', 127, 'Pop', 292501);
INSERT INTO Song
VALUES (9, 1, 'Dogs Don''t Suck', 193, 'Dance', 12521521);
INSERT INTO Song
VALUES (10, 1, 'Would You', 209, 'Pop', 24121453);
INSERT INTO Song
VALUES (10, 2, 'Only Me', 124, 'Pop', 9358249);
INSERT INTO Song
VALUES (10, 3, 'Be Fine', 158, 'Pop', 29209590);
INSERT INTO Song
VALUES (10, 4, 'Blue UU', 148, 'Pop', 428948429);
INSERT INTO Song
VALUES (11, 1, 'Sonder', 294, 'Alternative', 2392498);
INSERT INTO Song
VALUES (12, 1, 'Not A Chance', 359, 'Trance', 29485290);
INSERT INTO Song
VALUES (13, 1, 'Zeka', 180, 'Bluegrass', 2727920);
INSERT INTO Song
VALUES (14, 1, 'Basic', 153, 'Pop', 2727920);


INSERT INTO CardTable
VALUES ('Visa', 2394235323332438, '2025-01-01');
INSERT INTO CardTable
VALUES ('Amex', 5434235323332438, '2026-01-01');
INSERT INTO CardTable
VALUES ('Mastercard', 1234353233324332, '2027-01-01');
INSERT INTO CardTable
VALUES ('Visa', 7893353233324342, '2025-02-01');
INSERT INTO CardTable
VALUES ('Amex', 8989353233324023, '2023-07-01');

INSERT INTO PostalCodeCityProvince
VALUES ('Vancouver', 'BC', 'V6E7F3');
INSERT INTO PostalCodeCityProvince
VALUES ('Vancouver', 'BC', 'V4E7F3');
INSERT INTO PostalCodeCityProvince
VALUES ('Vancouver', 'BC' ,'V6E5E3');
INSERT INTO PostalCodeCityProvince
VALUES ('Vancouver', 'BC', 'V6E789');
INSERT INTO PostalCodeCityProvince
VALUES ('Vancouver', 'BC', 'V8E7K3');
INSERT INTO PostalCodeCityProvince
VALUES ('Vancouver', 'BC', 'V6E7K3');
INSERT INTO PostalCodeCityProvince
VALUES ('Vancouver', 'BC', 'V6E1K2');
INSERT INTO PostalCodeCityProvince
VALUES ('Calgary', 'AB', 'G5E5C6');
INSERT INTO PostalCodeCityProvince
VALUES ('Winnipeg', 'MB', 'H4R7J8');
INSERT INTO PostalCodeCityProvince
VALUES ('Toronto', 'ON', 'K9L0H1');


INSERT INTO BillingAddress
VALUES (3235, 'Joyce st.', 'V6E7F3');
INSERT INTO BillingAddress
VALUES (1234, 'Joyce st.', 'V6E7F3');
INSERT INTO BillingAddress
VALUES (1234, 'Cambie st.', 'V4E7F3');
INSERT INTO BillingAddress
VALUES (2829, 'Cambie st.', 'V6E7F3');
INSERT INTO BillingAddress
VALUES (7843, 'Cambie st.', 'V6E7F3');
INSERT INTO BillingAddress
VALUES (3235, 'Courtney St.', 'V6E5E3');
INSERT INTO BillingAddress
VALUES (1234, 'Camosun St.', 'V6E789');
INSERT INTO BillingAddress
VALUES (2948, 'Cambie st.', 'V4E7F3');
INSERT INTO BillingAddress
VALUES (1234, 'Hastings st.', 'V8E7K3');
INSERT INTO BillingAddress
VALUES (7843, 'Manitoba st.', 'V6E1K2');
INSERT INTO BillingAddress
VALUES (2345, 'Kenneth st.', 'G5E5C6');
INSERT INTO BillingAddress
VALUES (8343, 'Montgomery st.', 'H4R7J8');
INSERT INTO BillingAddress
VALUES (8583, 'Elliot st.', 'K9L0H1');

INSERT INTO UserPayment
VALUES (1, 7, '2022-03-01', 2394235323332438, 'bryanh', 3235, 'Joyce st.', 'V6E7F3');
INSERT INTO UserPayment
VALUES (2, 11, '2022-03-01', 5434235323332438, 'sleepy', 1234, 'Joyce st.', 'V6E7F3');
INSERT INTO UserPayment
VALUES (3, 12, '2022-02-01', 1234353233324332, 'A113', 1234, 'Cambie st.', 'V4E7F3');
INSERT INTO UserPayment
VALUES (4, 15, '2022-02-01', 7893353233324342, 'notstevejobs', 1234, 'Cambie st.', 'V4E7F3');
INSERT INTO UserPayment
VALUES (5, 15, '2022-02-01', 8989353233324023, 'celloist', 7843, 'Cambie st.', 'V6E7F3');

INSERT INTO CompanyPayment
VALUES (1, 7000, '2022-03-01', 3235, 'Courtney St.', 'V6E5E3', 'Sony Music');
INSERT INTO CompanyPayment
VALUES (2, 1100, '2022-03-01', 1234, 'Camosun St.', 'V6E789', 'Studio Bangi');
INSERT INTO CompanyPayment
VALUES (3, 1200, '2022-02-01', 1234, 'Cambie st.', 'V4E7F3', 'BMG Rights Management (UK) Limited');
INSERT INTO CompanyPayment
VALUES (4, 1500, '2022-02-01', 1234, 'Hastings st.', 'V8E7K3', 'Interscope Geffen (A&M) Records');
INSERT INTO CompanyPayment
VALUES (5, 1500, '2022-02-01', 7843, 'Manitoba st.', 'V6E1K2', 'Mellowdrama Records');

INSERT INTO Playlist
VALUES ('bryanh', 'lofi');
INSERT INTO Playlist
VALUES ('eenie meenie', '90s jam');
INSERT INTO Playlist
VALUES ('kimdol', 'study-session');
INSERT INTO Playlist
VALUES ('jfk1975', '80s glam metal');
INSERT INTO Playlist
VALUES ('A113', 'alt jam');

INSERT INTO PlaylistIsIn
VALUES ('bryanh', 'lofi', 1, 1);
INSERT INTO PlaylistIsIn
VALUES ('bryanh', 'lofi', 1, 2);
INSERT INTO PlaylistIsIn
VALUES ('kimdol', 'study-session', 2, 4);
INSERT INTO PlaylistIsIn
VALUES ('eenie meenie', '90s jam', 4, 4);
INSERT INTO PlaylistIsIn
VALUES ('jfk1975', '80s glam metal', 3, 1);

INSERT INTO AddsToLibrary
VALUES (1, 1, 'bryanh', 0, 1);
INSERT INTO AddsToLibrary
VALUES (1, 2, 'bryanh', 0, 0);
INSERT INTO AddsToLibrary
VALUES (2, 4, 'kimdol', 0, 1);
INSERT INTO AddsToLibrary
VALUES (4, 4, 'sleepy', 1, 0);
INSERT INTO AddsToLibrary
VALUES (3, 1, 'A113', 1, 1);
INSERT INTO AddsToLibrary
VALUES (4, 1, 'A113', 1, 1);
INSERT INTO AddsToLibrary
VALUES (4, 1, 'admin', 1, 1);
INSERT INTO AddsToLibrary
VALUES (4, 1, 'eenie meenie', 1, 1);
INSERT INTO AddsToLibrary
VALUES (4, 1, 'bryanh', 1, 1);
INSERT INTO AddsToLibrary
VALUES (4, 1, 'harperk', 1, 1);
INSERT INTO AddsToLibrary
VALUES (4, 1, 'dhrubok', 1, 1);
INSERT INTO AddsToLibrary
VALUES (4, 1, 'kimdol', 1, 1);
INSERT INTO AddsToLibrary
VALUES (4, 1, 'celloist', 1, 1);
INSERT INTO AddsToLibrary
VALUES (4, 1, 'notstevejobs', 1, 1);
INSERT INTO AddsToLibrary
VALUES (4, 1, 'jfk1975', 1, 1);
INSERT INTO AddsToLibrary
VALUES (4, 1, 'sleepy', 1, 1);
INSERT INTO AddsToLibrary
VALUES (3, 1, 'sleepy', 1, 1);

INSERT INTO FeaturedIn
VALUES (1, 1, 1);
INSERT INTO FeaturedIn
VALUES (2, 1, 2);
INSERT INTO FeaturedIn
VALUES (3, 2, 4);
INSERT INTO FeaturedIn
VALUES (4, 4, 4);
INSERT INTO FeaturedIn
VALUES (5, 3, 1);

