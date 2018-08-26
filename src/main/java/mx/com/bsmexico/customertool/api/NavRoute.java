package mx.com.bsmexico.customertool.api;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

//forward 
/**
 * @author jchr
 *
 */
public class NavRoute {

	private String section;
	private LinkedList<NavNode> path;
	private ListIterator<NavNode> itr;

	private NavRoute(final BuilderNavRoute builder) {
		path = new LinkedList<NavNode>();
		this.section = builder.section;
		path.addAll(builder.path);
		itr = path.listIterator();
	}

	public static class BuilderNavRoute {
		private String section;
		private List<NavNode> path;

		/**
		 * 
		 */
		public BuilderNavRoute() {
			this("");
		}

		/**
		 * @param section
		 */
		public BuilderNavRoute(final String section) {
			path = new ArrayList<NavNode>();
			this.section = section;
		}

		/**
		 * @param node
		 * @return
		 */
		public BuilderNavRoute addNode(final NavNode node) {
			path.add(node);
			return this;
		}

		/**
		 * @param title
		 * @return
		 */
		public BuilderNavRoute addNode(final String name, final String title) {
			this.addNode(name, title, -1, false, null);
			return this;
		}

		/**
		 * @param title
		 * @param position
		 * @param displace
		 * @param img
		 * @return
		 */
		public BuilderNavRoute addNode(final String name, final String title, final Integer position,
				final boolean displace, final InputStream img) {
			this.addNode(new NavNode(name, title, img, position, displace));
			return this;
		}

		/**
		 * @return
		 * @throws IllegalStateException
		 */
		public NavRoute build() throws IllegalStateException {
			if (path.isEmpty()) {
				throw new IllegalStateException("Is necesary add at least one node Node to build a NavRoute");
			}
			return new NavRoute(this);
		}
	}

	public static class NavNode {
		private String name;
		private String title;
		private InputStream img;
		private Integer position;
		private boolean displace;

		/**
		 * @param title
		 * @param img
		 * @param position
		 * @param displace
		 */
		public NavNode(String name, String title, InputStream img, Integer position, boolean displace) {
			super();
			this.name = (StringUtils.isBlank(name) ? UUID.randomUUID().toString() : name);
			this.title = (StringUtils.isBlank(title) ? StringUtils.EMPTY : title);
			this.img = img;
			this.position = position;
			this.displace = displace;
		}

		/**
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * @return the img
		 */
		public InputStream getImg() {
			return img;
		}

		/**
		 * @return the position
		 */
		public Integer getPosition() {
			return position;
		}

		/**
		 * @return the displace
		 */
		public boolean isDisplace() {
			return displace;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

	}

	/**
	 * Get previous node
	 * 
	 * @return previous node in the Route, if current node is the first element then
	 *         return null
	 */
	public NavNode previous() {
		NavNode node = null;
		try {
			node = itr.previous();
		} catch (NoSuchElementException exception) {
			// do nothing.
		}
		return node;
	}

	/**
	 * Get new node
	 * 
	 * @return get new node in the Route, if current node is the first element then
	 *         return null
	 */
	public NavNode next() {
		NavNode node = null;
		try {
			node = itr.next();
		} catch (NoSuchElementException exception) {
			// do nothing.
		}
		return node;
	}

	/**
	 * After calling this method, the call next() should return the first node in
	 * the Route
	 * 
	 * @return
	 */
	public void goStart() {
		itr = path.listIterator();
	}

	/**
	 * After calling this method, the call previous() should return the last node in
	 * the Route
	 * 
	 * @return
	 */
	public void goEnd() {
		while (itr.hasNext())
			itr.next();
	}

	/**
	 * @return the section
	 */
	public String getSection() {
		return section;
	}

	/**
	 * @return
	 */
	public boolean hasNext() {
		return itr.hasNext();
	}

	/**
	 * @return
	 */
	public boolean hasPrevious() {
		return itr.hasPrevious();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getPathString(path);
	}

	/**
	 * @return
	 */
	public String toStringFromCurrentPosition() {
		return getPathString(path.subList(itr.nextIndex(), this.path.size() - 1));
	}

	private String getPathString(final List<NavNode> nodes) {
		String result = StringUtils.EMPTY;
		if (nodes != null && !nodes.isEmpty()) {
			final List<String> stringPath = new ArrayList<>();
			stringPath.add(StringUtils.EMPTY);
			nodes.forEach(node -> stringPath.add(node.title));
			result = StringUtils.join(stringPath, "/");
		}
		return result;
	}
}
