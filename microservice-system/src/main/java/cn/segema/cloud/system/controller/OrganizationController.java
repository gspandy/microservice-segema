package cn.segema.cloud.system.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.segema.cloud.common.page.Pager;
import cn.segema.cloud.common.page.PagerParamVO;
import cn.segema.cloud.system.domain.Organization;
import cn.segema.cloud.system.repository.OrganizationRepository;
import cn.segema.cloud.system.vo.OrganizationPersonalVO;
import cn.segema.cloud.system.vo.OrganizationTreeVO;

@RestController
@RequestMapping(value = "/organization")
public class OrganizationController {
	@Autowired
	private DiscoveryClient discoveryClient;
	@Autowired
	private OrganizationRepository organizationRepository;

	/**
	 * @param id
	 * @return organizationRepository信息
	 */
	@GetMapping("/{userId}")
	public Organization findById(@PathVariable String userId) throws Exception {
		Organization organization = organizationRepository.findOne(userId);
		return organization;
	}

	@GetMapping("/list")
	public List<Organization> list(Organization organization, Model model) {
		List<Organization> organizationList = organizationRepository.findAll();
		return organizationList;
	}

	@PostMapping("/add")
	public Organization add(Organization organization, Model model) {
		// if (user.getUserId() == null || "".equals(user.getUserId())) {
		// user.setUserId(UUID.randomUUID().toString());
		// }
		organizationRepository.save(organization);
		return organization;
	}

	@RequestMapping(value = "edit")
	public Organization edit(Organization organization, Model model) {
		// Role oldRole = roleRepository.getOne(role.getRoleId());
		// BeanUtils.copyProperties(role, oldRole);
		organizationRepository.save(organization);
		return organization;
	}

	@RequestMapping(value = "delete")
	public Organization delete(Organization organization) {
		organizationRepository.delete(organization);
		return organization;
	}

	@GetMapping("/listOrganizationPersonalByName/{organizationName}")
	public List<OrganizationPersonalVO> listOrganizationPersonalByName(@PathVariable String organizationName) {
		List<OrganizationPersonalVO> organizationList = organizationRepository
				.findOrganizationPersonalByName(organizationName);
		return organizationList;
	}

	@GetMapping("/listByPage")
	public Pager<Organization> listByPage(PagerParamVO pagerParam) {
		Sort sort = new Sort(Direction.DESC, "organizationId");
		Pageable pageable = new PageRequest(pagerParam.getCurr() - 1, pagerParam.getNums(), sort);
		Page<Organization> page = organizationRepository.findAll(pageable);
		Pager<Organization> pager = new Pager<Organization>();
		pager.setCode("0");
		pager.setMsg("success");
		pager.setCount(page.getTotalElements());
		pager.setData(page.getContent());
		return pager;
	}
	
	@GetMapping("/maxOrganization/{parentOrganizationCode}")
	public Integer maxOrganization(@PathVariable Integer parentOrganizationCode) {
		Integer organizationCode = organizationRepository.findMaxOrganization(parentOrganizationCode);
		return organizationCode;
	}
	
	@GetMapping("/treeList")
	public List<OrganizationTreeVO> treeList(@PathVariable Integer parentOrganizationCode) {
		List<OrganizationTreeVO> organizationList = organizationRepository.findTreeList(parentOrganizationCode);
		return organizationList;
	}

	/**
	 * 本地服务实例的信息
	 * 
	 * @return
	 */
	@GetMapping("/instance-info")
	public ServiceInstance showInfo() {
		ServiceInstance localServiceInstance = this.discoveryClient.getLocalServiceInstance();
		return localServiceInstance;
	}
}