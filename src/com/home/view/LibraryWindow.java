package com.home.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import com.home.model.Genres;
import com.home.model.Book;
import com.home.viewmodel.BookManager;
import com.home.viewmodel.Library;
/*
 * Created by JFormDesigner on Fri May 06 19:36:43 YEKT 2022
 */



/**
 * @author get
 */
public class LibraryWindow extends JFrame implements Observer{		
	public Library library;
	public Book currentBook;
	
	public LibraryWindow(Library library) {
		library.addObserver(this);
		this.library = library;
		initComponents();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		//Library libr = (Library) o;
		mainList.setListData(library.getList().toArray(new String[0]));
		
		bookReviewPanel.removeAll();
		bookReviewPanel.updateUI();
	}
	
	public void viewBook(Book book) {
		bookReviewPanel.removeAll();
		bookReviewPanel.updateUI();
		JTextField idField = new JTextField("Id: " + book.getId());
		JTextField usernameField = new JTextField("Title: " + book.getTitle());
		JTextField authorField = new JTextField("Author: " + book.getAuthor());
		JTextField genresField = new JTextField("Genre: " + book.getGenre());
		JTextField pagesField = new JTextField("Pages: " + book.getPages());
		
		JTextArea descriptionField = new JTextArea("Description: " + book.getDescription(), 5, 5);
		descriptionField.setEditable(false);
		JScrollPane description = new JScrollPane(descriptionField);
		description.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		description.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		description.setAutoscrolls(true);
		
		JTextField ratingField = new JTextField("Rating: " + book.getRating());
		
		JButton viewButton = new JButton("VIEW");
		viewButton.addActionListener(new ViewListener());
		
		JTextField nameFile = new JTextField("File: " + BookManager.getAddress(book.getFile()));
		nameFile.setEditable(false);
		
		JButton browseUrlButton = new JButton("BROWSE URL");
		browseUrlButton.addActionListener(new BrowseUrlListener());
		
		JTextField nameUrl = new JTextField("URL: " + book.getUrl());
		nameUrl.setEditable(false);
					
		JTextComponent[] message = {
				idField,
				usernameField,
				authorField,
				genresField,
				pagesField,
				ratingField,
		};
		
		for(JTextComponent comp : message) {
			//comp.setEnabled(false);
			comp.setEditable(false);
			bookReviewPanel.add(comp);
		}
		
		bookReviewPanel.add(description);
		
		if(nameFile.getText().length() > 6) {
			bookReviewPanel.add(nameFile);
			bookReviewPanel.add(viewButton);
		}
		
		if(nameUrl.getText().length() > 5) {
			bookReviewPanel.add(nameUrl);
			bookReviewPanel.add(browseUrlButton);
		}
			
		this.validate();
	}
		
	private class DeleteListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			int option = JOptionPane.showConfirmDialog(null, "You really wanna delete?");
			if(option == JOptionPane.OK_OPTION) {
				if(currentBook != null && !mainList.isSelectionEmpty())  {					
					library.removeBook(currentBook);
				}
			}
		}
	}
	
	private class NewListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Show window with input areas
			JTextField usernameField = new JTextField();
			JTextField authorField = new JTextField();
			
			JComboBox<String> genresField = new JComboBox<>(Genres.getArray());
			genresField.setEditable(true);
			
			JTextField pagesField = new JTextField();
			
			JTextArea descriptionField = new JTextArea(5, 5);
			JScrollPane descriptionPane = new JScrollPane(descriptionField);
			descriptionPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			descriptionPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			descriptionPane.setAutoscrolls(true);
			
			Integer[] ratingAr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
			JComboBox<Integer> ratingField = new JComboBox<>(ratingAr);
			
			
			JLabel selectText = new JLabel();
			FileListener listener = new FileListener(selectText, null);
			JButton button = new JButton("SELECT FILE: ");
			button.addActionListener(listener);
									
			Object[] message = {
			    "Title:", usernameField,
			    "Author:", authorField,
			    "Genres:", genresField,
			    "Pages:", pagesField,
			    "Rating:", ratingField,
			    "Description:", descriptionPane,
			    button, selectText
			};

			int option = JOptionPane.showConfirmDialog(null, message, "New Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			
			if(option == JOptionPane.OK_OPTION) {
			    // Creating book by books' creator
				
				String title = usernameField.getText();
				String author = authorField.getText();
				String genre = (String) genresField.getSelectedItem();
				String pages = pagesField.getText();
				int rating = (int) ratingField.getSelectedItem();
				String description = descriptionField.getText();
				File file = listener.getSelected();
				
				Book book = BookManager.createBook(title, author, genre, pages, rating, description, file); //Need use address to file
				library.addBook(book);
				
				library.setUrl(book);
			}
		}
	}
		
	private class EditListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(currentBook != null & !mainList.isSelectionEmpty()) {
//				int bookId = BookManager.getIdFromRecord(mainList.getSelectedValue());
//				Book currentBook = library.getBookById(bookId);
				
				JTextField usernameField = new JTextField(currentBook.getTitle());
				JTextField authorField = new JTextField(currentBook.getAuthor());
				JComboBox<String> genresField = new JComboBox<>(Genres.getArray());
				genresField.setSelectedItem(currentBook.getGenre());
				genresField.setEditable(true);
				JTextField pagesField = new JTextField(Integer.toString(currentBook.getPages()));
				
				JTextArea descriptionField = new JTextArea(currentBook.getDescription(), 5, 5);
				JScrollPane descriptionPane = new JScrollPane(descriptionField);
				descriptionPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				descriptionPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				descriptionPane.setAutoscrolls(true);
				
				Integer[] ratingAr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
				JComboBox<Integer> ratingField = new JComboBox<>(ratingAr);
				ratingField.setSelectedItem(currentBook.getRating());
				
				JLabel selectText = new JLabel();
				FileListener listener = new FileListener(selectText, currentBook.getFile());
				JButton button = new JButton("SELECT FILE: ");
				button.addActionListener(listener);
							
				Object[] message = {
				    "Title:", usernameField,
				    "Author:", authorField,
				    "Genres:", genresField,
				    "Pages:", pagesField,
				    "Rating:", ratingField,
				    "Description:", descriptionPane,
				    button, selectText
				};

				int option = JOptionPane.showConfirmDialog(null, message, "New Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (option == JOptionPane.OK_OPTION) {
				    // Creating book by books' creator
									
					String title = usernameField.getText();
					String author = authorField.getText();
					String genre = (String) genresField.getSelectedItem();
					String pages = pagesField.getText();
					int rating = (int) ratingField.getSelectedItem();
					String description = descriptionField.getText();
					File file = listener.getSelected();
					String url = currentBook.getUrl();
										
					Book newBook = BookManager.createBookForReplace(currentBook, title, author, genre, pages, rating, description, file, url);

					library.editBook(currentBook, newBook);
					
					if(url.isEmpty())
						library.setUrl(newBook);
				}
			}
		}
	}
	
	private class ViewListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			library.openBook(currentBook);
		}
	}
	
	private class BrowseUrlListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			library.browseUrl(currentBook.getUrl());
		}
	}
		
	private class FileListener implements ActionListener {
		File file;
		JLabel text;
		
		FileListener(JLabel text, File file) {
			this.file = file;
			this.text = text;
			updateText();
		}
		
		private void updateText() {
			if(file != null) {
				text.setText("Selected: " + file.getName());
			}
		}
		
		public File getSelected() {
			return file;
		}
		
		@Override
		public void actionPerformed(ActionEvent ae) {
			JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
			int option = fileChooser.showOpenDialog(null);
			
			if(option == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
				updateText();
			}
		}
	}
		
	private class CheckerClicks extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
//			JList<String> list = (JList<String>) e.getSource();
			if(e.getClickCount() == 2 && !mainList.isSelectionEmpty()) {
				String record = mainList.getSelectedValue();
				//library.printBook(BookManager.getIdFromRecord(record));
				currentBook = library.getBookById(BookManager.getIdFromRecord(record));
				viewBook(currentBook);
			}
			super.mouseClicked(e);
		}
	}
	
	private class ImportListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
			int option = chooser.showOpenDialog(null);
			
			if(option == JFileChooser.APPROVE_OPTION) {
				library.mergeXML(chooser.getSelectedFile());
			}
			
		}
	}
	
	private class ExportListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
			int option = chooser.showSaveDialog(null);
			
			if(option == JFileChooser.APPROVE_OPTION) {
				library.export(chooser.getSelectedFile());
			}
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		menuBar1 = new JMenuBar();
		fileMenu = new JMenu();
		importXMLMenu = new JMenuItem();
		exportXMLMenu = new JMenuItem();
		viewMenu = new JMenu();
		booksMenu = new JMenuItem();
		authorsMenu = new JMenuItem();
		genresMenu = new JMenuItem();
		buttonsPanel = new JPanel();
		buttonNew = new JButton();
		buttonEdit = new JButton();
		buttonDelete = new JButton();
		mainPanel = new JPanel();
		scrollPane1 = new JScrollPane();
		mainList = new JList<>();
		bookReviewPanel = new JPanel();

		//======== this ========
		setTitle("Own Library");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== menuBar1 ========
		{

			//======== fileMenu ========
			{
				fileMenu.setText("File");

				//---- importXMLMenu ----
				importXMLMenu.setText("Import XML");
				fileMenu.add(importXMLMenu);

				//---- exportXMLMenu ----
				exportXMLMenu.setText("Export XML");
				fileMenu.add(exportXMLMenu);
			}
			menuBar1.add(fileMenu);

			//======== viewMenu ========
			{
				viewMenu.setText("View");

				//---- booksMenu ----
				booksMenu.setText("Books");
				viewMenu.add(booksMenu);

				//---- authorsMenu ----
				authorsMenu.setText("Authors");
				viewMenu.add(authorsMenu);

				//---- genresMenu ----
				genresMenu.setText("Genres");
				viewMenu.add(genresMenu);
			}
			menuBar1.add(viewMenu);
		}
		setJMenuBar(menuBar1);

		//======== buttonsPanel ========
		{
			buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

			//---- buttonNew ----
			buttonNew.setText("NEW");
			buttonNew.setMinimumSize(new Dimension(39, 25));
			buttonNew.setPreferredSize(new Dimension(153, 25));
			buttonNew.setIconTextGap(6);
			buttonNew.setMaximumSize(new Dimension(1920, 100));
			buttonsPanel.add(buttonNew);

			//---- buttonEdit ----
			buttonEdit.setText("EDIT");
			buttonEdit.setMaximumSize(new Dimension(1920, 100));
			buttonEdit.setPreferredSize(new Dimension(153, 25));
			buttonsPanel.add(buttonEdit);

			//---- buttonDelete ----
			buttonDelete.setText("DELETE");
			buttonDelete.setMaximumSize(new Dimension(1920, 100));
			buttonDelete.setPreferredSize(new Dimension(153, 25));
			buttonsPanel.add(buttonDelete);
		}
		contentPane.add(buttonsPanel, BorderLayout.SOUTH);

		//======== mainPanel ========
		{
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

			//======== scrollPane1 ========
			{
				scrollPane1.setMinimumSize(new Dimension(200, 23));
				scrollPane1.setPreferredSize(new Dimension(200, 146));

				//---- mainList ----
				mainList.setMinimumSize(new Dimension(200, 54));
				mainList.setPreferredSize(new Dimension(200, 54));
				mainList.setMaximumSize(new Dimension(20000, 54));
				scrollPane1.setViewportView(mainList);
			}
			mainPanel.add(scrollPane1);

			//======== bookReviewPanel ========
			{
				bookReviewPanel.setLayout(new BoxLayout(bookReviewPanel, BoxLayout.Y_AXIS));
			}
			mainPanel.add(bookReviewPanel);
		}
		contentPane.add(mainPanel, BorderLayout.CENTER);
		setSize(460, 410);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		
		mainList.addMouseListener(new CheckerClicks());
		buttonNew.addActionListener(new NewListener());
		buttonEdit.addActionListener(new EditListener());
		buttonDelete.addActionListener(new DeleteListener());
		importXMLMenu.addActionListener(new ImportListener());
		exportXMLMenu.addActionListener(new ExportListener());
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JMenuBar menuBar1;
	private JMenu fileMenu;
	private JMenuItem importXMLMenu;
	private JMenuItem exportXMLMenu;
	private JMenu viewMenu;
	private JMenuItem booksMenu;
	private JMenuItem authorsMenu;
	private JMenuItem genresMenu;
	private JPanel buttonsPanel;
	private JButton buttonNew;
	private JButton buttonEdit;
	private JButton buttonDelete;
	private JPanel mainPanel;
	private JScrollPane scrollPane1;
	private JList<String> mainList;
	private JPanel bookReviewPanel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
