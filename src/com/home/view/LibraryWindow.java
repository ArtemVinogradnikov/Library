package com.home.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
		JTextField idField = new JTextField("Id: " + book.getId());
		JTextField usernameField = new JTextField("Title: " + book.getTitle());
		JTextField authorField = new JTextField("Author: " + book.getAuthor());
		JTextField genresField = new JTextField("Genre: " + book.getGenre().getName());
		JTextField pagesField = new JTextField("Pages: " + book.getPages());
		
		JTextArea descriptionField = new JTextArea("Description: " + book.getDescription(), 5, 5);
		descriptionField.setEditable(false);
		JScrollPane description = new JScrollPane(descriptionField);
		description.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		description.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		description.setAutoscrolls(true);
		
		JTextField ratingField = new JTextField("Rating: " + book.getRating());
					
		Object[] message = {
				idField,
				usernameField,
				authorField,
				genresField,
				pagesField,
				ratingField
		};
		
		for(Object obj : message) {
			JTextComponent comp = (JTextComponent) obj;
			//comp.setEnabled(false);
			comp.setEditable(false);
			bookReviewPanel.add(comp);
		}
		
		bookReviewPanel.add(description);
		
		this.validate();
	}
	
	private class DeleteListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int bookId = BookManager.getIdFromRecord(mainList.getSelectedValue());
			Book currentBook = library.getBookById(bookId);
			
			library.removeBook(currentBook);
		}
	}
	
	private class NewListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Show window with input areas
			JTextField usernameField = new JTextField();
			JTextField authorField = new JTextField();
			JComboBox<Genres> genresField = new JComboBox<>(Genres.values());
			JTextField pagesField = new JTextField();
			
			JTextArea descriptionField = new JTextArea(5, 5);
			JScrollPane description = new JScrollPane(descriptionField);
			description.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			description.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			description.setAutoscrolls(true);
			
			Integer[] rating = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
			JComboBox<Integer> ratingField = new JComboBox<>(rating);
						
			Object[] message = {
			    "Title:", usernameField,
			    "Author:", authorField,
			    "Genres:", genresField,
			    "Pages:", pagesField,
			    "Rating:", ratingField,
			    "Description:", description
			};

			int option = JOptionPane.showConfirmDialog(null, message, "New Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (option == JOptionPane.OK_OPTION) {
			    // Creating book by books' creator
				Book book = BookManager.createBook(usernameField.getText(), authorField.getText(),(Genres) genresField.getSelectedItem(), 
						Integer.parseInt(pagesField.getText()), (int) ratingField.getSelectedItem(), descriptionField.getText());
				library.addBook(book);
			}
		}
	}
	
	private class EditListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!mainList.isSelectionEmpty()) {
				int bookId = BookManager.getIdFromRecord(mainList.getSelectedValue());
				Book currentBook = library.getBookById(bookId);
				
				JTextField usernameField = new JTextField(currentBook.getTitle());
				JTextField authorField = new JTextField(currentBook.getAuthor());
				JComboBox<Genres> genresField = new JComboBox<>(Genres.values());
				genresField.setSelectedItem(currentBook.getGenre());
				JTextField pagesField = new JTextField(Integer.toString(currentBook.getPages()));
				
				JTextArea descriptionField = new JTextArea(currentBook.getDescription(), 5, 5);
				JScrollPane description = new JScrollPane(descriptionField);
				description.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				description.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				description.setAutoscrolls(true);
				
				Integer[] rating = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
				JComboBox<Integer> ratingField = new JComboBox<>(rating);
				ratingField.setSelectedItem(currentBook.getRating());
							
				Object[] message = {
				    "Title:", usernameField,
				    "Author:", authorField,
				    "Genres:", genresField,
				    "Pages:", pagesField,
				    "Rating:", ratingField,
				    "Description:", description
				};

				int option = JOptionPane.showConfirmDialog(null, message, "New Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (option == JOptionPane.OK_OPTION) {
				    // Creating book by books' creator
					currentBook.setTitle(usernameField.getText());
					currentBook.setAuthor(authorField.getText());
					currentBook.setGenre((Genres) genresField.getSelectedItem());
					currentBook.setPages(Integer.parseInt(pagesField.getText()));
					currentBook.setRating((int) ratingField.getSelectedItem());
					currentBook.setDescription(descriptionField.getText());
				}
				
				library.editBook(currentBook);
			}
		}
	}
	
	private class CheckerClicks extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
//			JList<String> list = (JList<String>) e.getSource();
			if(e.getClickCount() == 2 && !mainList.isSelectionEmpty()) {
				String record = (String) mainList.getSelectedValue();
				//library.printBook(BookManager.getIdFromRecord(record));
				viewBook(library.getBookById(BookManager.getIdFromRecord(record)));
			}
			// TODO Auto-generated method stub
			super.mouseClicked(e);
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		menuBar1 = new JMenuBar();
		viewMenu = new JMenu();
		booksMenu = new JMenuItem();
		authorsMenu = new JMenuItem();
		genresMenu = new JMenuItem();
		scrollPane1 = new JScrollPane();
		mainList = new JList<>();
		bookReviewPanel = new JPanel();
		buttonsPanel = new JPanel();
		buttonNew = new JButton();
		buttonEdit = new JButton();
		buttonDelete = new JButton();

		//======== this ========
		setTitle("Own Library");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== menuBar1 ========
		{

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

		//======== scrollPane1 ========
		{
			scrollPane1.setViewportView(mainList);
		}
		contentPane.add(scrollPane1, BorderLayout.CENTER);

		//======== bookReviewPanel ========
		{
			bookReviewPanel.setPreferredSize(new Dimension(250, 0));
			bookReviewPanel.setMinimumSize(new Dimension(150, 0));
			bookReviewPanel.setLayout(new BoxLayout(bookReviewPanel, BoxLayout.Y_AXIS));
		}
		contentPane.add(bookReviewPanel, BorderLayout.EAST);

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
		setSize(460, 410);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		
		mainList.addMouseListener(new CheckerClicks());
		buttonNew.addActionListener(new NewListener());
		buttonEdit.addActionListener(new EditListener());
		buttonDelete.addActionListener(new DeleteListener());
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JMenuBar menuBar1;
	private JMenu viewMenu;
	private JMenuItem booksMenu;
	private JMenuItem authorsMenu;
	private JMenuItem genresMenu;
	private JScrollPane scrollPane1;
	private JList<String> mainList;
	private JPanel bookReviewPanel;
	private JPanel buttonsPanel;
	private JButton buttonNew;
	private JButton buttonEdit;
	private JButton buttonDelete;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
