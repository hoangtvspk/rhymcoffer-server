// Global variables
let currentSection = 'dashboard';
let currentEditItem = null;
let currentEditType = null;

// Initialize the dashboard
$(document).ready(function() {
    // Load initial data
    loadDashboardData();
    
    // Set up event listeners
    setupEventListeners();
});

// Set up all event listeners
function setupEventListeners() {
    // Navigation links
    $('.nav-link').click(function(e) {
        e.preventDefault();
        const section = $(this).data('section');
        switchSection(section);
    });

    // Refresh button
    $('#refreshBtn').click(function() {
        loadCurrentSectionData();
    });

    // Save changes button in modal
    $('#save-changes').click(function() {
        saveChanges();
    });
}

// Switch between sections
function switchSection(section) {
    // Update active nav link
    $('.nav-link').removeClass('active');
    $(`.nav-link[data-section="${section}"]`).addClass('active');

    // Hide all sections
    $('.section').hide();

    // Show selected section
    $(`#${section}-section`).show();

    // Update current section
    currentSection = section;

    // Load section data
    loadCurrentSectionData();
}

// Load data for current section
function loadCurrentSectionData() {
    switch(currentSection) {
        case 'dashboard':
            loadDashboardData();
            break;
        case 'users':
            loadUsers();
            break;
        case 'artists':
            loadArtists();
            break;
        case 'albums':
            loadAlbums();
            break;
        case 'tracks':
            loadTracks();
            break;
        case 'playlists':
            loadPlaylists();
            break;
    }
}

// Load dashboard data
function loadDashboardData() {
    // Load users count
    $.get('/api/admin/users')
        .done(function(users) {
            $('#total-users').text(users.length);
        });

    // Load artists count
    $.get('/api/admin/artists')
        .done(function(artists) {
            $('#total-artists').text(artists.length);
        });

    // Load albums count
    $.get('/api/admin/albums')
        .done(function(albums) {
            $('#total-albums').text(albums.length);
        });

    // Load tracks count
    $.get('/api/admin/tracks')
        .done(function(tracks) {
            $('#total-tracks').text(tracks.length);
        });
}

// Load users data
function loadUsers() {
    $.get('/api/admin/users')
        .done(function(users) {
            const tbody = $('#users-table-body');
            tbody.empty();
            
            users.forEach(function(user) {
                tbody.append(`
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${user.email}</td>
                        <td>${user.displayName || '-'}</td>
                        <td>
                            <div class="btn-group">
                                <button class="btn btn-sm btn-primary" onclick="editItem('user', ${user.id})">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <button class="btn btn-sm btn-danger" onclick="deleteItem('user', ${user.id})">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </div>
                        </td>
                    </tr>
                `);
            });
        });
}

// Load artists data
function loadArtists() {
    $.get('/api/admin/artists')
        .done(function(artists) {
            const tbody = $('#artists-table-body');
            tbody.empty();
            
            artists.forEach(function(artist) {
                tbody.append(`
                    <tr>
                        <td>${artist.id}</td>
                        <td>${artist.name}</td>
                        <td>${artist.popularity}</td>
                        <td>
                            <div class="btn-group">
                                <button class="btn btn-sm btn-primary" onclick="editItem('artist', ${artist.id})">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <button class="btn btn-sm btn-danger" onclick="deleteItem('artist', ${artist.id})">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </div>
                        </td>
                    </tr>
                `);
            });
        });
}

// Load albums data
function loadAlbums() {
    $.get('/api/admin/albums')
        .done(function(albums) {
            const tbody = $('#albums-table-body');
            tbody.empty();
            
            albums.forEach(function(album) {
                tbody.append(`
                    <tr>
                        <td>${album.id}</td>
                        <td>${album.name}</td>
                        <td>${album.artistName || '-'}</td>
                        <td>${album.releaseDate || '-'}</td>
                        <td>
                            <div class="btn-group">
                                <button class="btn btn-sm btn-primary" onclick="editItem('album', ${album.id})">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <button class="btn btn-sm btn-danger" onclick="deleteItem('album', ${album.id})">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </div>
                        </td>
                    </tr>
                `);
            });
        });
}

// Load tracks data
function loadTracks() {
    $.get('/api/admin/tracks')
        .done(function(tracks) {
            const tbody = $('#tracks-table-body');
            tbody.empty();
            
            tracks.forEach(function(track) {
                tbody.append(`
                    <tr>
                        <td>${track.id}</td>
                        <td>${track.name}</td>
                        <td>${track.artistName || '-'}</td>
                        <td>${track.albumName || '-'}</td>
                        <td>${formatDuration(track.durationMs)}</td>
                        <td>
                            <div class="btn-group">
                                <button class="btn btn-sm btn-primary" onclick="editItem('track', ${track.id})">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <button class="btn btn-sm btn-danger" onclick="deleteItem('track', ${track.id})">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </div>
                        </td>
                    </tr>
                `);
            });
        });
}

// Load playlists data
function loadPlaylists() {
    $.get('/api/admin/playlists')
        .done(function(playlists) {
            const tbody = $('#playlists-table-body');
            tbody.empty();
            
            playlists.forEach(function(playlist) {
                tbody.append(`
                    <tr>
                        <td>${playlist.id}</td>
                        <td>${playlist.name}</td>
                        <td>${playlist.ownerName || '-'}</td>
                        <td>${playlist.isPublic ? 'Yes' : 'No'}</td>
                        <td>${playlist.collaborative ? 'Yes' : 'No'}</td>
                        <td>
                            <div class="btn-group">
                                <button class="btn btn-sm btn-primary" onclick="editItem('playlist', ${playlist.id})">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <button class="btn btn-sm btn-danger" onclick="deleteItem('playlist', ${playlist.id})">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </div>
                        </td>
                    </tr>
                `);
            });
        });
}

// Edit item
function editItem(type, id) {
    currentEditType = type;
    currentEditItem = id;
    
    // Load item data
    $.get(`/api/admin/${type}s/${id}`)
        .done(function(item) {
            // Create form based on type
            let form = createEditForm(type, item);
            
            // Show modal with form
            $('#edit-modal-body').html(form);
            $('#editModal').modal('show');
        });
}

// Create edit form based on type
function createEditForm(type, item) {
    switch(type) {
        case 'user':
            return `
                <form id="edit-form">
                    <div class="form-group">
                        <label class="form-label">Username</label>
                        <input type="text" class="form-control" name="username" value="${item.username}" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Email</label>
                        <input type="email" class="form-control" name="email" value="${item.email}" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Display Name</label>
                        <input type="text" class="form-control" name="displayName" value="${item.displayName || ''}">
                    </div>
                </form>
            `;
        case 'artist':
            return `
                <form id="edit-form">
                    <div class="form-group">
                        <label class="form-label">Name</label>
                        <input type="text" class="form-control" name="name" value="${item.name}" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Popularity</label>
                        <input type="number" class="form-control" name="popularity" value="${item.popularity}" required>
                    </div>
                </form>
            `;
        // Add more cases for other types...
    }
}

// Save changes
function saveChanges() {
    const formData = new FormData($('#edit-form')[0]);
    const data = Object.fromEntries(formData.entries());
    
    $.ajax({
        url: `/api/admin/${currentEditType}s/${currentEditItem}`,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function() {
            $('#editModal').modal('hide');
            loadCurrentSectionData();
        },
        error: function(xhr) {
            alert('Error saving changes: ' + xhr.responseText);
        }
    });
}

// Delete item
function deleteItem(type, id) {
    if (confirm(`Are you sure you want to delete this ${type}?`)) {
        $.ajax({
            url: `/api/admin/${type}s/${id}`,
            method: 'DELETE',
            success: function() {
                loadCurrentSectionData();
            },
            error: function(xhr) {
                alert('Error deleting item: ' + xhr.responseText);
            }
        });
    }
}

// Format duration in milliseconds to MM:SS
function formatDuration(ms) {
    if (!ms) return '-';
    const minutes = Math.floor(ms / 60000);
    const seconds = Math.floor((ms % 60000) / 1000);
    return `${minutes}:${seconds.toString().padStart(2, '0')}`;
} 