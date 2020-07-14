/**
 * File Name        	: PlatformRepository.java
 * File Path        	: /kosign-push-api/src/main/java/com/kosign/push/platforms/PlatformRepository.java
 * File Description 	: 
 * 
 * File Author	  		: Neng Channa
 * Created Date	  	    : 13-July-2020 13:40
 * Developed By	  	    : Sok Menghok
 * Modified Date	  	: 13-July-2020 16:29
 * Modified By          : Sok Menghok
 *
 **/

package com.kosign.push.platforms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends JpaRepository<PlatformEntity,String>{}