drop schema bettrack;
create schema bettrack;
use bettrack;

create table BT_Team(
	team_id int primary key auto_increment,
    name varchar(30)
);


create table BT_TeamAlias(
	team_alias varchar(30) primary key,
    team_id int,
    foreign key team_alias_team_fk (team_id) references BT_Team(team_id)
);



create table BT_Bookie(
	bookie_id int primary key auto_increment,
    bookie_name varchar(20)
);

create table BT_Match(
	match_id int primary key auto_increment,
    home_team int, 
    away_team int,
    match_date timestamp(0),
    foreign key match_teamhome_fk(home_team) references BT_Team(team_id),
    foreign key match_teamaway_fk(away_team) references BT_Team(team_id)
);

create table BT_Event(
	event_id int primary key auto_increment,
    event_name varchar(20) not null -- 1x2, 12, over/under
);


create table BT_EventAlias(
	name varchar(50) primary key,
    event_id int,
	foreign key event_alias_event_fk (event_id) references BT_Event(event_id)
);


create table BT_EventMatch(
	event_id int not null,
	match_id int not null,
    primary key(event_id, match_id),
    foreign key eventmatch_event_fk(event_id) references BT_Event(event_id),
    foreign key eventmatch_match_fk(match_id) references BT_Match(match_id)
);


create table BT_Bet(
	bookie_id int,
	event_id int,
    match_id int,
    bet_result varchar(20) not null, -- 
    odd numeric(4,2) not null,
    from_date timestamp not null,
    to_date timestamp,
    unique(bookie_id, event_id, match_id, bet_result),
    foreign key bet_bookie_fk(bookie_id) references BT_Bookie(bookie_id),
    foreign key bet_eventmatch(event_id, match_id) references BT_eventmatch(event_id,match_id)
);


create view BTv_Match as
	(select home.name as home_team,
			away.name as away_team, 
            btm.match_date
     from BT_Match btm
	 join BT_Team home on home.team_id = btm.home_team
     join BT_Team away on away.team_id = btm.home_team
     order by btm.match_date desc);


create view BTv_MatchEvent as
	(select home.name as home_team,
			away.name as away_name,
            bte.event_name
	 from BT_EventMatch em
     join BT_Event bte on em.event_id = bte.event_id
     join BT_Match btm on em.match_id = em.match_id
     join BT_Team home on home.team_id = btm.home_team
     join BT_Team away on away.team_id = btm.home_team);
     
create view BTv_MatchEventBet as
	(select home.name as home_team,
			away.name as away_name,
            btm.match_date,
            bte.event_name,
            bet.bet_result,
            bet.odd
	 from BT_EventMatch em
     join BT_Event bte on em.event_id = bte.event_id
     join BT_Match btm on em.match_id = em.match_id
     join BT_Team home on home.team_id = btm.home_team
     join BT_Team away on away.team_id = btm.home_team
     join BT_Bet bet   on bet.event_id = em.event_id
					   and bet.match_id = em.match_id);


 -- PROCEDURES/FUNCTIONS

DELIMITER $$
CREATE PROCEDURE GetMatchEvents(IN p_home_team varchar(30), in p_away_team varchar(30), in p_match_date timestamp)
BEGIN
 select home_team,
		away_team,
        match_date,
        event_name,
        bet_result,
        odd 
 from BTv_MatchEventBet
 where home_team = p_home_team
	   and away_team = p_away_team
       and match_date = p_match_date;
    END$$
DELIMITER ;     


DELIMITER $$
CREATE FUNCTION GetTeamIDByName(p_team_alias varchar(30))
RETURNS int
/*Function return team id based on team alias*/
BEGIN
	declare r_team_id int;

	select team_id into r_team_id 
	from BT_TeamAlias 
	where team_alias = p_team_alias;
    
    return r_team_id;
END$$
DELIMITER ;     

 
 DELIMITER $$
CREATE FUNCTION GetMatchID(p_team_home_id int, p_team_away_id int, p_date timestamp)
RETURNS int
/*Function return match id based on teams and date*/
BEGIN
	declare r_match_id int;

	select match_id into r_match_id 
	from BT_Match 
    where home_team = p_team_home_id
		  and away_team = p_team_away_id
          and match_date = p_date;
    
    return r_match_id;
END$$
DELIMITER ; 


DELIMITER $$
CREATE FUNCTION InsertMatch(p_home_team varchar(30), p_away_team varchar(30), p_match_date timestamp)
RETURNS boolean
/* Function check if match exsist and return match id. If match not exists function insert new match and return new match id */
BEGIN
	DECLARE match_id int;
	DECLARE home_team_id int;
	DECLARE away_team_id int;
    
    
    set home_team_id = GetTeamIDByName(p_home_team);
    set away_team_id = GetTeamIDByName(p_away_team);
    
    set match_id = getMatchID(home_team_id, away_team_id, p_match_date);
          
    if match_id is not null then
        return match_id;
	end if;
    
    insert into BT_Match(home_team, away_team, match_date) values(home_team_id, away_team_id, p_match_date);
    return last_insert_id();    
END$$
DELIMITER ;     



DELIMITER $$
CREATE FUNCTION GetEventIDByName(p_event_name varchar(20))
RETURNS int
/* Function return event id based on event alias */
BEGIN
	  declare r_event_id int;
      select event_id into r_event_id from BT_EventAlias;
      
      return r_event_id;
END$$
DELIMITER ; 



DELIMITER $$
CREATE FUNCTION GetBookieID(bookie_name varchar(20))
/* Function add new bet with result and odd  I.E chelse - man. city -> event :1x2, result:chelsea, odd: 2.1 */
RETURNS int
BEGIN
	declare r_bookie_id int;
    select bookie_id into r_bookie_id from BT_Bookie where bookie_name = bookie_name;  
    return r_bookie_id;
END$$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE InsertEventOdd(in p_match_id int, in p_event_name varchar(20), in p_bet_result varchar(20), in p_odd decimal(4,2), in bookie_name varchar(20))
/* Function add new bet with result and odd  I.E chelse - man. city -> event :1x2, result:chelsea, odd: 2.1 */
proc:BEGIN
	declare _event_id int;
      
    set _event_id = GetEventIDByName(p_event_name);
      
	if _event_id is null then
		LEAVE proc;
	end if;
      
    insert into BT_Bet(bookie_id, event_id, match_id, bet_result, odd, from_date, to_date)
			 values (_bookie_id, _event_id, p_match_id, p_bet_result, p_odd, current_timestamp(), null); 
             
END$$
DELIMITER ;   



 -- TRIGGERS

DELIMITER $$
CREATE TRIGGER UpdateBetHistory 
BEFORE INSERT ON BT_Bet
FOR EACH ROW
trig:BEGIN
	declare odd_cmp decimal(4,2);
    
	select odd into odd_cmp 
    from BT_Bet 
    where bookie_id = NEW.bookie_id
		  and event_id = NEW.event_id
          and match_id = NEW.match_id
          and bet_result = NEW.bet_result
          and to_date is null;
    
    if (odd_cmp is null) then
		LEAVE trig; 			 -- no bet to modify 
	end if;
	
    if(odd_cmp = NEW.odd) then 
		signal sqlstate '45000'; -- abort
	end if;
          
	update BT_Bet
 	set to_date = current_timestamp()
    where bookie_id = NEW.bookie_id
		  and event_id = NEW.event_id
          and match_id = NEW.match_id
          and bet_result = NEW.bet_result
          and to_date is null;
END$$
DELIMITER ; 